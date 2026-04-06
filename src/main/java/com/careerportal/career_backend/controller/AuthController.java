package com.careerportal.career_backend.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.careerportal.career_backend.dto.GoogleAuthRequest;
import com.careerportal.career_backend.dto.RegisterRequest;
import com.careerportal.career_backend.entity.User;
import com.careerportal.career_backend.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
@Validated
public class AuthController {

    @Autowired
    private UserService userService;

    // 🔹 Register (student)
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(Map.of("message", userService.registerUser(request)));
    }

    // 🔹 Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody RegisterRequest request) {

        User user = userService.findByEmail(request.getEmail());

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "User not found"));
        }

        if (!userService.verifyPassword(user, request.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Wrong password"));
        }

        return ResponseEntity.ok(Map.of("user", userService.toUserResponse(user)));
    }

    @PostMapping("/google")
    public ResponseEntity<?> googleAuth(@Valid @RequestBody GoogleAuthRequest request) {
        try {
            User user = userService.loginWithGoogle(request.getToken(), request.getRole());
            return ResponseEntity.ok(Map.of("user", userService.toUserResponse(user)));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Google authentication failed", "error", ex.getMessage()));
        }
    }

    @GetMapping("/users")
    public ResponseEntity<Map<String, List<Map<String, Object>>>> getUsers() {
        return ResponseEntity.ok(Map.of("users", userService.getAllUsers().stream()
                .map(userService::toUserResponse)
                .collect(Collectors.toList())));
    }

    @GetMapping("/users/students")
    public ResponseEntity<Map<String, List<Map<String, Object>>>> getStudents() {
        return ResponseEntity.ok(Map.of("users", userService.getAllStudents().stream()
                .map(userService::toUserResponse)
                .collect(Collectors.toList())));
    }

    // 🔹 Delete user by ID
    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
    }
}