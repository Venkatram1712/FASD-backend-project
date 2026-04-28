package com.careerportal.career_backend.controller;

import com.careerportal.career_backend.dto.ErrorResponse;
import com.careerportal.career_backend.dto.QuestionnaireResponseDTO;
import com.careerportal.career_backend.dto.SuccessResponse;
import com.careerportal.career_backend.entity.QuestionnaireResponse;
import com.careerportal.career_backend.service.QuestionnaireService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/questionnaire")
public class QuestionnaireResponseController {

    private final QuestionnaireService questionnaireService;

    public QuestionnaireResponseController(QuestionnaireService questionnaireService) {
        this.questionnaireService = questionnaireService;
    }

    @PostMapping("/responses")
    public ResponseEntity<?> saveResponse(@RequestBody QuestionnaireResponseDTO dto) {
        try {
            QuestionnaireResponse saved = questionnaireService.save(dto);
            return ResponseEntity.ok(
                    new SuccessResponse("RESPONSE_SAVED", "Questionnaire response saved successfully", saved.getId()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("INVALID_INPUT", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("SAVE_FAILED", "Failed to save questionnaire response: " + e.getMessage()));
        }
    }

    @GetMapping("/responses/user/{userId}")
    public ResponseEntity<?> getResponsesByUser(@PathVariable Long userId) {
        try {
            List<QuestionnaireResponse> responses = questionnaireService.findByUserId(userId);
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("FETCH_FAILED", "Failed to fetch responses: " + e.getMessage()));
        }
    }

    @GetMapping("/responses/user/{userId}/latest")
    public ResponseEntity<?> getLatestResponse(@PathVariable Long userId) {
        try {
            return questionnaireService.findLatestByUserId(userId)
                    .<ResponseEntity<?>>map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("FETCH_FAILED", "Failed to fetch response: " + e.getMessage()));
        }
    }

    @GetMapping("/responses/{id}")
    public ResponseEntity<?> getResponseById(@PathVariable Long id) {
        try {
            return questionnaireService.findById(id)
                    .<ResponseEntity<?>>map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("FETCH_FAILED", "Failed to fetch response: " + e.getMessage()));
        }
    }
}
