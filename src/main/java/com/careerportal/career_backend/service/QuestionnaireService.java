package com.careerportal.career_backend.service;

import com.careerportal.career_backend.dto.QuestionnaireResponseDTO;
import com.careerportal.career_backend.entity.QuestionnaireResponse;
import com.careerportal.career_backend.repository.QuestionnaireResponseRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class QuestionnaireService {

    private final QuestionnaireResponseRepository responseRepository;

    public QuestionnaireService(QuestionnaireResponseRepository responseRepository) {
        this.responseRepository = responseRepository;
    }

    @Transactional
    public QuestionnaireResponse save(QuestionnaireResponseDTO dto) {
        if (dto.getUserId() == null || dto.getUserId() <= 0) {
            throw new IllegalArgumentException("User ID is required and must be positive");
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

        return responseRepository.save(response);
    }

    @Transactional(readOnly = true)
    public List<QuestionnaireResponse> findByUserId(Long userId) {
        return responseRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public Optional<QuestionnaireResponse> findLatestByUserId(Long userId) {
        return responseRepository.findFirstByUserIdOrderBySubmittedAtDesc(userId);
    }

    @Transactional(readOnly = true)
    public Optional<QuestionnaireResponse> findById(Long id) {
        return responseRepository.findById(id);
    }
}
