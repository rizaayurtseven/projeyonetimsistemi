package com.stajprojesi.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.Set;

@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Ad boş bırakılamaz")
    private String firstName;
    
    @NotBlank(message = "Soyad boş bırakılamaz")
    private String lastName;
    
    @Email(message = "Geçerli bir e-posta adresi girin")
    private String email;

    @ManyToMany(mappedBy = "employees")
    private Set<Project> projects;

    // Getter ve Setterlar
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Set<Project> getProjects() { return projects; }
    public void setProjects(Set<Project> projects) { this.projects = projects; }
} 