package com.careerportal.career_backend.controller;

import com.careerportal.career_backend.dto.QuestionnaireResponseDTO;
import com.careerportal.career_backend.entity.QuestionnaireResponse;
import com.careerportal.career_backend.repository.QuestionnaireResponseRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/questionnaire")
@CrossOrigin
public class QuestionnaireResponseController {

    @Autowired
    private QuestionnaireResponseRepository responseRepository;

    @PostMapping("/responses")
    public ResponseEntity<?> saveResponse(@RequestBody QuestionnaireResponseDTO dto) {
        try {
            if (dto.getUserId() == null || dto.getUserId() <= 0) {
                return ResponseEntity.badRequest().body(
                        new ErrorResponse("INVALID_USER", "User ID is required and must be positive"));
            }

            QuestionnaireResponse response = new QuestionnaireResponse();
            response.setUserId(dto.getUserId());
            response.setInterests(dto.getInterests());
            response.setStrengths(dto.getStrengths());
            response.setCareerGoals(dto.getCareerGoals());
            response.setEducationLevel(dto.getEducationLevel());
            response.setIndustries(dto.getIndustries());
            response.setWorkStyle(dto.getWorkStyle());
            response.setSkills(dto.getSkills());
            response.setTimeline(dto.getTimeline());
            response.setSubmittedAt(dto.getSubmittedAt() != null ? dto.getSubmittedAt() : LocalDateTime.now());

            QuestionnaireResponse saved = responseRepository.save(response);

            return ResponseEntity.ok(
                    new SuccessResponse("RESPONSE_SAVED", "Questionnaire response saved successfully", saved.getId()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("SAVE_FAILED", "Failed to save questionnaire response: " + e.getMessage()));
        }
    }

    @GetMapping("/responses/user/{userId}")
    public ResponseEntity<?> getResponsesByUser(@PathVariable Long userId) {
        try {
            List<QuestionnaireResponse> responses = responseRepository.findByUserId(userId);
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("FETCH_FAILED", "Failed to fetch responses: " + e.getMessage()));
        }
    }

    @GetMapping("/responses/user/{userId}/latest")
    public ResponseEntity<?> getLatestResponse(@PathVariable Long userId) {
        try {
            Optional<QuestionnaireResponse> response =
                    responseRepository.findFirstByUserIdOrderBySubmittedAtDesc(userId);
            return response.<ResponseEntity<?>>map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("FETCH_FAILED", "Failed to fetch response: " + e.getMessage()));
        }
    }

    @GetMapping("/responses/{id}")
    public ResponseEntity<?> getResponseById(@PathVariable Long id) {
        try {
            Optional<QuestionnaireResponse> response = responseRepository.findById(id);
            return response.<ResponseEntity<?>>map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("FETCH_FAILED", "Failed to fetch response: " + e.getMessage()));
        }
    }

    public static class SuccessResponse {
        private String status;
        private String message;
        private Object data;

        public SuccessResponse(String status, String message, Object data) {
            this.status = status;
            this.message = message;
            this.data = data;
        }

        public String getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }

        public Object getData() {
            return data;
        }
    }

    public static class ErrorResponse {
        private String error;
        private String message;

        public ErrorResponse(String error, String message) {
            this.error = error;
            this.message = message;
        }

        public String getError() {
            return error;
        }

        public String getMessage() {
            return message;
        }
    }
}
