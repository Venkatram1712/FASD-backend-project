package com.careerportal.career_backend.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.careerportal.career_backend.dto.RegisterRequest;
import com.careerportal.career_backend.entity.User;
import com.careerportal.career_backend.service.UserService;

@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {

    @Autowired
    private UserService userService;

    // 🔹 Register (student)
    @PostMapping({ "/register", "/signup" })
    public ResponseEntity<Map<String, Object>> register(@RequestBody RegisterRequest request) {
        User createdUser = userService.registerUser(request);
        return ResponseEntity.ok(Map.of(
                "message", "User registered successfully!",
                "user", userService.toUserResponse(createdUser)));
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