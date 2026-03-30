package com.stajprojesi.backend.service;

import com.stajprojesi.backend.model.Role;
import com.stajprojesi.backend.model.User;
import com.stajprojesi.backend.repository.RoleRepository;
import com.stajprojesi.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User createUser(String username, String email, String password) {
        if (username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty() ||
            email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Kullanıcı adı, şifre ve e-posta boş bırakılamaz.");
        }

        username = username.trim();
        email = email.trim();
        password = password.trim();

        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalStateException("Kullanıcı adı zaten mevcut.");
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalStateException("E-posta zaten kayıtlı.");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);

        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Varsayılan 'USER' rolü bulunamadı."));
        user.setRoles(Collections.singleton(userRole));

        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı: " + username));
    }
}
