package com.careerportal.career_backend.repository;

import com.careerportal.career_backend.entity.CareerPath;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CareerPathRepository extends JpaRepository<CareerPath, Long> {
}
