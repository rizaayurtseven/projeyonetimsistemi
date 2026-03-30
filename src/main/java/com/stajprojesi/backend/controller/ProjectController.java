package com.stajprojesi.backend.controller;

import com.stajprojesi.backend.dto.ProjectDto;
import com.stajprojesi.backend.model.Employee;
import com.stajprojesi.backend.model.Project;
import com.stajprojesi.backend.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    private ProjectDto convertToDto(Project project) {
        ProjectDto dto = new ProjectDto();
        dto.setId(project.getId());
        dto.setName(project.getName());
        dto.setDescription(project.getDescription());
        dto.setStatus(project.getStatus());
        if (project.getOwner() != null) {
            ProjectDto.OwnerDto ownerDto = new ProjectDto.OwnerDto();
            ownerDto.setUsername(project.getOwner().getUsername());
            dto.setOwner(ownerDto);
        }
        if (project.getFiles() != null) {
            Set<ProjectDto.FileDto> fileDtos = project.getFiles().stream().map(f -> {
                ProjectDto.FileDto fdto = new ProjectDto.FileDto();
                fdto.setId(f.getId());
                fdto.setFileName(f.getFileName());
                fdto.setFileType(f.getFileType());
                fdto.setFilePath(f.getFilePath());
                return fdto;
            }).collect(Collectors.toSet());
            dto.setFiles(fileDtos);
        }
        return dto;
    }

    @GetMapping
    public ResponseEntity<List<ProjectDto>> getAllProjects() {
        List<ProjectDto> projects = projectService.getAllProjects().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDto> getProjectById(@PathVariable Long id) {
        return ResponseEntity.ok(convertToDto(projectService.getProjectById(id)));
    }

    @PostMapping
    public ResponseEntity<ProjectDto> createProject(@Valid @RequestBody Project project) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null ? authentication.getName() : null;
        return ResponseEntity.ok(convertToDto(projectService.createProject(project, username)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectDto> updateProject(@PathVariable Long id, @Valid @RequestBody Project updatedProject) {
        return ResponseEntity.ok(convertToDto(projectService.updateProject(id, updatedProject)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null ? authentication.getName() : null;
        projectService.deleteProject(id, username);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{projectId}/assign/{employeeId}")
    public ResponseEntity<ProjectDto> assignEmployeeToProject(@PathVariable Long projectId, @PathVariable Long employeeId) {
        return ResponseEntity.ok(convertToDto(projectService.assignEmployee(projectId, employeeId)));
    }

    @DeleteMapping("/{projectId}/unassign/{employeeId}")
    public ResponseEntity<ProjectDto> unassignEmployeeFromProject(@PathVariable Long projectId, @PathVariable Long employeeId) {
        return ResponseEntity.ok(convertToDto(projectService.unassignEmployee(projectId, employeeId)));
    }

    @GetMapping("/{projectId}/employees")
    public ResponseEntity<Set<Employee>> getEmployeesOfProject(@PathVariable Long projectId) {
        return ResponseEntity.ok(projectService.getProjectEmployees(projectId));
    }

    @PostMapping("/{projectId}/uploadFile")
    public ResponseEntity<String> uploadFile(@PathVariable Long projectId, @RequestParam("file") MultipartFile file) {
        try {
            String result = projectService.uploadFile(projectId, file);
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Dosya yüklenemedi: IO Hatası");
        }
    }
}
