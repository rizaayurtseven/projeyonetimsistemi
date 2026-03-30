package com.stajprojesi.backend.service;

import com.stajprojesi.backend.model.Employee;
import com.stajprojesi.backend.model.Project;
import com.stajprojesi.backend.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Çalışan bulunamadı: " + id));
    }

    public Employee createEmployee(Employee employee) {
        if (employee.getFirstName() == null || employee.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("Çalışan adı boş olamaz.");
        }
        if (employee.getLastName() == null || employee.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Çalışan soyadı boş olamaz.");
        }
        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(Long id, Employee employeeDetails) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Çalışan bulunamadı: " + id));
        employee.setFirstName(employeeDetails.getFirstName());
        employee.setLastName(employeeDetails.getLastName());
        employee.setEmail(employeeDetails.getEmail());
        return employeeRepository.save(employee);
    }

    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new RuntimeException("Çalışan bulunamadı: " + id);
        }
        employeeRepository.deleteById(id);
    }

    public Set<Project> getEmployeeProjects(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Çalışan bulunamadı: " + id));
        return employee.getProjects();
    }
}
