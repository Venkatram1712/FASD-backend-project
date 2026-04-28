package com.careerportal.career_backend.service;

import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.careerportal.career_backend.dto.UpdateProfileRequest;
import com.careerportal.career_backend.dto.RegisterRequest;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import com.careerportal.career_backend.entity.User;
import com.careerportal.career_backend.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Data
public class UserService {

    private static final BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();
    @Autowired
    private UserRepository userRepository;

    //  Register (for students)
    public User registerUser(RegisterRequest request) {

        if (request == null) {
            throw new IllegalArgumentException("Request body is required");
        }

        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name is required");
        }

        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }

        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }

        String normalizedEmail = normalizeEmail(request.getEmail());

        if (userRepository.findByEmailIgnoreCase(normalizedEmail).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = new User();
        user.setName(request.getName() == null ? null : request.getName().trim());
        user.setEmail(normalizedEmail);
        user.setPassword(PASSWORD_ENCODER.encode(request.getPassword()));
        user.setProvider("local");
        user.setQuestionnaireCompleted(false);
        user.setStatus("active");

        // Default to STUDENT unless the provided role is valid.
        user.setRole(parseRole(request.getRole()));

        return userRepository.save(user);
    }

    // 🔹 Find user
    public User findByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email).orElse(null);
    }

    public boolean verifyPassword(User user, String rawPassword) {
        if (user == null || rawPassword == null) {
            return false;
        }

        String storedPassword = user.getPassword();
        if (storedPassword == null) {
            return false;
        }

        // Backward compatibility for old plaintext records.
        if (!storedPassword.startsWith("$2a$") && !storedPassword.startsWith("$2b$") && !storedPassword.startsWith("$2y$")) {
            return storedPassword.equals(rawPassword);
        }

        return PASSWORD_ENCODER.matches(rawPassword, storedPassword);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getAllStudents() {
        return userRepository.findByRoleIgnoreCase("STUDENT");
    }

    // 🔹 Delete user by ID
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public User updateProfile(Long userId, UpdateProfileRequest req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String normalizedEmail = normalizeEmail(req.getEmail());
        if (userRepository.existsByEmailIgnoreCaseAndIdNot(normalizedEmail, userId)) {
            throw new RuntimeException("Email already exists");
        }

        user.setName(req.getName().trim());
        user.setEmail(normalizedEmail);
        user.setPhone(trimOrNull(req.getPhone()));
        user.setBio(trimOrNull(req.getBio()));
        user.setInstitution(trimOrNull(req.getInstitution()));
        user.setSpecialization(trimOrNull(req.getSpecialization()));
        user.setExperienceYears(req.getExperienceYears());

        if (req.getQuestionnaireCompleted() != null) {
            user.setQuestionnaireCompleted(req.getQuestionnaireCompleted());
        }

        if (req.getAssignedCounselorId() != null) {
            if (!"STUDENT".equalsIgnoreCase(user.getRole())) {
                throw new IllegalArgumentException("Only students can be assigned a counselor");
            }

            if (req.getAssignedCounselorId() <= 0) {
                user.setAssignedCounselorId(null);
                return userRepository.save(user);
            }

            User counselor = userRepository.findById(req.getAssignedCounselorId())
                    .orElseThrow(() -> new IllegalArgumentException("Assigned counselor not found"));

            if (!"COUNSELOR".equalsIgnoreCase(counselor.getRole())) {
                throw new IllegalArgumentException("Assigned user must have counselor role");
            }

            user.setAssignedCounselorId(counselor.getId());
        }

        return userRepository.save(user);
    }

    public Map<String, Object> toUserResponse(User user) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("id", user.getId());
        response.put("name", nullable(user.getName()));
        response.put("email", nullable(user.getEmail()));
        response.put("role", nullable(user.getRole()));
        response.put("status", nullable(user.getStatus()));
        response.put("specialization", nullable(user.getSpecialization()));
        response.put("phone", nullable(user.getPhone()));
        response.put("bio", nullable(user.getBio()));
        response.put("institution", nullable(user.getInstitution()));
        response.put("experienceYears", user.getExperienceYears());
        response.put("questionnaireCompleted", user.getQuestionnaireCompleted() == null ? false : user.getQuestionnaireCompleted());
        response.put("assignedCounselorId", user.getAssignedCounselorId());
        return response;
    }

    public String parseRole(String rawRole) {
        if (rawRole == null || rawRole.trim().isEmpty()) {
            return "STUDENT";
        }

        String normalized = rawRole.trim().toUpperCase();
        return switch (normalized) {
            case "ADMIN", "COUNSELOR", "STUDENT" -> normalized;
            default -> "STUDENT";
        };
    }

    private String normalizeEmail(String email) {
        if (email == null) {
            return null;
        }
        return email.trim().toLowerCase();
    }

    private String trimOrNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private Object nullable(Object value) {
        return value == null ? "" : value;
    }
}