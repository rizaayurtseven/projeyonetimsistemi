package com.stajprojesi.backend.controller;

import com.stajprojesi.backend.model.User;
import com.stajprojesi.backend.model.Role;
import com.stajprojesi.backend.service.UserService;
import com.stajprojesi.backend.dto.UserResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/users") // Admin'e özel endpoint
public class UserController {

    @Autowired
    private UserService userService;

    // Sadece ADMIN rolüne sahip kullanıcılar bu endpoint'e erişebilir
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> createUser(@RequestBody User newUser) {
        userService.createUser(newUser.getUsername(), newUser.getEmail(), newUser.getPassword());
        return new ResponseEntity<>("Kullanıcı başarıyla eklendi.", HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserResponseDto> dtoList = users.stream().map(u -> {
            UserResponseDto dto = new UserResponseDto();
            dto.setId(u.getId());
            dto.setUsername(u.getUsername());
            dto.setEmail(u.getEmail());
            dto.setRoles(u.getRoles().stream().map(Role::getName).collect(Collectors.toSet()));
            return dto;
        }).collect(Collectors.toList());
        
        return ResponseEntity.ok(dtoList);
    }
}