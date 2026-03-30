package com.stajprojesi.backend.service;

import com.stajprojesi.backend.model.Project;
import com.stajprojesi.backend.repository.EmployeeRepository;
import com.stajprojesi.backend.repository.ProjectFileRepository;
import com.stajprojesi.backend.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectService projectService;

    private Project mockProject;

    @BeforeEach
    void setUp() {
        mockProject = new Project();
        mockProject.setId(1L);
        mockProject.setName("Test Project");
        mockProject.setDescription("This is a test project");
    }

    @Test
    void getProjectById_Success() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(mockProject));

        Project result = projectService.getProjectById(1L);

        assertNotNull(result);
        assertEquals("Test Project", result.getName());
        verify(projectRepository, times(1)).findById(1L);
    }

    @Test
    void getProjectById_NotFound() {
        when(projectRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            projectService.getProjectById(99L);
        });
        
        verify(projectRepository, times(1)).findById(99L);
    }

    @Test
    void updateProject_Success() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(mockProject));
        when(projectRepository.save(any(Project.class))).thenReturn(mockProject);

        Project updateDetails = new Project();
        updateDetails.setName("Updated Project");
        updateDetails.setDescription("Updated desc");
        updateDetails.setStatus(Project.Status.DEVAM_EDIYOR);

        Project result = projectService.updateProject(1L, updateDetails);

        assertNotNull(result);
        assertEquals("Updated Project", result.getName());
        assertEquals("Updated desc", result.getDescription());
        assertEquals(Project.Status.DEVAM_EDIYOR, result.getStatus());
        verify(projectRepository, times(1)).save(mockProject);
    }
}
