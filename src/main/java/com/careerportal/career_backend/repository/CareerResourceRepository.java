package com.careerportal.career_backend.repository;

import com.careerportal.career_backend.entity.CareerResource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CareerResourceRepository extends JpaRepository<CareerResource, Long> {
}
