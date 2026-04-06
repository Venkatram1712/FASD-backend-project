package com.careerportal.career_backend.service;

import com.careerportal.career_backend.dto.CareerPathRequest;
import com.careerportal.career_backend.entity.CareerPath;
import com.careerportal.career_backend.repository.CareerPathRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CareerPathService {

    private final CareerPathRepository careerPathRepository;

    public CareerPathService(CareerPathRepository careerPathRepository) {
        this.careerPathRepository = careerPathRepository;
    }

    public List<CareerPath> getAll() {
        return careerPathRepository.findAll();
    }

    public CareerPath create(CareerPathRequest request) {
        CareerPath path = new CareerPath();
        path.setTitle(request.getTitle() == null ? "" : request.getTitle().trim());
        path.setCategory(request.getCategory() == null ? "General" : request.getCategory().trim());
        path.setSummary(request.getSummary() == null ? null : request.getSummary().trim());
        return careerPathRepository.save(path);
    }

    public CareerPath update(Long id, CareerPathRequest request) {
        CareerPath existing = careerPathRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Career path not found: " + id));

        if (request.getTitle() != null) {
            existing.setTitle(request.getTitle().trim());
        }
        if (request.getCategory() != null) {
            existing.setCategory(request.getCategory().trim());
        }
        if (request.getSummary() != null) {
            existing.setSummary(request.getSummary().trim());
        }

        return careerPathRepository.save(existing);
    }

    public void delete(Long id) {
        careerPathRepository.deleteById(id);
    }
}
