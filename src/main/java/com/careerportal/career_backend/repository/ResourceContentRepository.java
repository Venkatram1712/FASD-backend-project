package com.careerportal.career_backend.repository;

import com.careerportal.career_backend.entity.ResourceContent;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceContentRepository extends JpaRepository<ResourceContent, Long> {

    List<ResourceContent> findByResourceIdOrderByIdAsc(Long resourceId);
}
