package com.careerportal.career_backend.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.careerportal.career_backend.dto.LoginRequest;
import com.careerportal.career_backend.dto.RegisterRequest;
import com.careerportal.career_backend.entity.User;
import com.careerportal.career_backend.security.JwtService;
import com.careerportal.career_backend.service.UserService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {
    private final UserService userService;
    private final JwtService jwtService;

    public AuthController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    // 🔹 Register (student)
    @PostMapping({ "/register", "/signup" })
    public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody RegisterRequest request) {
        User createdUser = userService.registerUser(request);
        return ResponseEntity.ok(Map.of(
                "message", "User registered successfully!",
                "user", userService.toUserResponse(createdUser)));
    }

    // 🔹 Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {

        User user = userService.findByEmail(request.getEmail());

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "User not found"));
        }

        if (!userService.verifyPassword(user, request.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Wrong password"));
        }

        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(Map.of(
                "token", token,
                "tokenType", "Bearer",
                "expiresAt", jwtService.calculateExpirationInstant().toString(),
                "user", userService.toUserResponse(user)));
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

    // 🔹 Recent activity feed — returns last 6 signup events from DB
    @GetMapping("/activity")
    public ResponseEntity<List<Map<String, Object>>> getRecentActivity() {
        List<Map<String, Object>> feed = userService.getAllUsers().stream()
                .sorted((a, b) -> Long.compare(
                        b.getId() != null ? b.getId() : 0L,
                        a.getId() != null ? a.getId() : 0L))
                .limit(6)
                .map(u -> Map.<String, Object>of(
                        "id", String.valueOf(u.getId()),
                        "type", "signup",
                        "message", (u.getEmail() != null ? u.getEmail() : "user") + " signed up as " + (u.getRole() != null ? u.getRole().toLowerCase() : "student"),
                        "createdAt", java.time.LocalDateTime.now().minusDays(u.getId() != null ? u.getId() % 30 : 0).toString()
                ))
                .collect(java.util.stream.Collectors.toList());
        return ResponseEntity.ok(feed);
    }
}