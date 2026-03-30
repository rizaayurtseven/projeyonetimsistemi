package com.stajprojesi.backend.service;

import com.stajprojesi.backend.model.Employee;
import com.stajprojesi.backend.model.Project;
import com.stajprojesi.backend.model.ProjectFile;
import com.stajprojesi.backend.model.User;
import com.stajprojesi.backend.repository.EmployeeRepository;
import com.stajprojesi.backend.repository.ProjectFileRepository;
import com.stajprojesi.backend.repository.ProjectRepository;
import com.stajprojesi.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ProjectFileRepository projectFileRepository;

    @Autowired
    private UserRepository userRepository;

    private static final List<String> ALLOWED_EXTENSIONS = List.of(
            "pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx",
            "txt", "png", "jpg", "jpeg", "gif", "zip", "rar"
    );

    public List<Project> getAllProjects() {
        return projectRepository.findAllWithFiles();
    }

    public Project getProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proje bulunamadı: " + id));
    }

    public Project createProject(Project project, String username) {
        if (project.getName() == null || project.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Proje adı boş olamaz.");
        }
        User owner = userRepository.findByUsername(username).orElse(null);
        if (owner != null) {
            project.setOwner(owner);
        }
        return projectRepository.save(project);
    }

    public Project updateProject(Long id, Project updatedProject) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proje bulunamadı: " + id));
        project.setName(updatedProject.getName());
        project.setDescription(updatedProject.getDescription());
        project.setStatus(updatedProject.getStatus());
        return projectRepository.save(project);
    }

    public void deleteProject(Long id, String username) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proje bulunamadı: " + id));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new SecurityException("Yetkisiz işlem."));

        boolean isAdmin = user.getRoles().stream()
                .anyMatch(r -> r.getName().equals("ADMIN") || r.getName().equals("ROLE_ADMIN"));
        boolean isOwner = project.getOwner() != null && project.getOwner().getUsername().equals(username);

        if (!isAdmin && !isOwner) {
            throw new SecurityException("Bu projeyi silme yetkiniz yok.");
        }
        projectRepository.deleteById(id);
    }

    public Project assignEmployee(Long projectId, Long employeeId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Proje bulunamadı: " + projectId));
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Çalışan bulunamadı: " + employeeId));
        project.getEmployees().add(employee);
        return projectRepository.save(project);
    }

    public Project unassignEmployee(Long projectId, Long employeeId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Proje bulunamadı: " + projectId));
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Çalışan bulunamadı: " + employeeId));
        project.getEmployees().remove(employee);
        return projectRepository.save(project);
    }

    public Set<Employee> getProjectEmployees(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Proje bulunamadı: " + projectId));
        return project.getEmployees();
    }

    public String uploadFile(Long projectId, MultipartFile file) throws IOException {
        if (file.getSize() > 10_000_000) {
            throw new IllegalArgumentException("Dosya çok büyük! (Maks: 10MB)");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new IllegalArgumentException("Geçersiz dosya adı!");
        }

        String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException("Bu dosya türü desteklenmiyor! İzin verilen: " + String.join(", ", ALLOWED_EXTENSIONS));
        }

        String fileName = UUID.randomUUID() + "_" + originalFilename;
        Path uploadPath = Paths.get("uploads");
        if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Proje bulunamadı: " + projectId));

        ProjectFile projectFile = new ProjectFile();
        projectFile.setFileName(originalFilename);
        projectFile.setFileType(file.getContentType());
        projectFile.setFilePath(filePath.toString());
        projectFile.setProject(project);
        projectFileRepository.save(projectFile);

        return "Dosya yüklendi";
    }
}
