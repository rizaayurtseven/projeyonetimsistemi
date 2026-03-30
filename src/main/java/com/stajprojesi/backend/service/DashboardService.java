package com.stajprojesi.backend.service;

import com.stajprojesi.backend.repository.EmployeeRepository;
import com.stajprojesi.backend.repository.ProjectFileRepository;
import com.stajprojesi.backend.repository.ProjectRepository;
import com.stajprojesi.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectFileRepository projectFileRepository;

    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        
        long totalProjects = projectRepository.count();
        long totalEmployees = employeeRepository.count();
        long totalUsers = userRepository.count();
        long totalFiles = projectFileRepository.count();
        
        stats.put("totalProjects", totalProjects);
        stats.put("totalEmployees", totalEmployees);
        stats.put("totalUsers", totalUsers);
        stats.put("totalFiles", totalFiles);
        
        return stats;
    }
}
