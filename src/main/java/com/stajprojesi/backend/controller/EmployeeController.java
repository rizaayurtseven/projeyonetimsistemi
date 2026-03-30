package com.stajprojesi.backend.controller;

import com.stajprojesi.backend.model.Employee;
import com.stajprojesi.backend.model.Project;
import com.stajprojesi.backend.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable Long id) {
        return employeeService.getEmployeeById(id);
    }

    @PostMapping
    public Employee createEmployee(@jakarta.validation.Valid @RequestBody Employee employee) {
        return employeeService.createEmployee(employee);
    }

    @PutMapping("/{id}")
    public Employee updateEmployee(@PathVariable Long id, @jakarta.validation.Valid @RequestBody Employee employeeDetails) {
        return employeeService.updateEmployee(id, employeeDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
    }

    @GetMapping("/{id}/projects")
    public Set<Project> getProjectsOfEmployee(@PathVariable Long id) {
        return employeeService.getEmployeeProjects(id);
    }
} 