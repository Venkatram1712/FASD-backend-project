package com.careerportal.career_backend.repository;

import com.careerportal.career_backend.entity.QuestionnaireResponse;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionnaireResponseRepository extends JpaRepository<QuestionnaireResponse, Long> {

    List<QuestionnaireResponse> findByUserId(Long userId);

    Optional<QuestionnaireResponse> findFirstByUserIdOrderBySubmittedAtDesc(Long userId);
}
