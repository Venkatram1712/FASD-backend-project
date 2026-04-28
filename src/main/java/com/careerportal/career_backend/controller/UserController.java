package com.careerportal.career_backend.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.careerportal.career_backend.dto.UpdateProfileRequest;
import com.careerportal.career_backend.entity.User;
import com.careerportal.career_backend.repository.UserRepository;
import com.careerportal.career_backend.service.UserService;

import jakarta.validation.Valid;

@RestController
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping({ "/api/users", "/api/admin/users" })
    public ResponseEntity<Map<String, Object>> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size) {
        if (size > 200) size = 200;
        Page<User> userPage = userRepository.findAll(PageRequest.of(page, size, Sort.by("id").ascending()));
        return ResponseEntity.ok(Map.of(
                "users", userPage.getContent().stream().map(userService::toUserResponse).collect(Collectors.toList()),
                "totalElements", userPage.getTotalElements(),
                "totalPages", userPage.getTotalPages(),
                "page", page
        ));
    }

    @GetMapping({ "/api/users/students" })
    public ResponseEntity<Map<String, List<Map<String, Object>>>> getStudents() {
        return ResponseEntity.ok(Map.of("users", userService.getAllStudents().stream()
                .map(userService::toUserResponse)
                .collect(Collectors.toList())));
    }

    @GetMapping({ "/api/users/{id}", "/api/auth/users/{id}", "/api/admin/users/{id}" })
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        return userService.getAllUsers().stream()
                .filter(user -> id != null && id.equals(user.getId()))
                .findFirst()
                .<ResponseEntity<?>>map(user -> ResponseEntity.ok(Map.of("user", userService.toUserResponse(user))))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "User not found")));
    }

    @PutMapping({ "/api/users/{id}", "/api/auth/users/{id}", "/api/admin/users/{id}" })
    public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody UpdateProfileRequest request) {
        try {
            User updated = userService.updateProfile(id, request);
            return ResponseEntity.ok(Map.of("user", userService.toUserResponse(updated)));
        } catch (RuntimeException ex) {
            if ("User not found".equals(ex.getMessage())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", ex.getMessage()));
            }
            if ("Email already exists".equals(ex.getMessage())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", ex.getMessage()));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Failed to update user", "error", ex.getMessage()));
        }
    }
}
