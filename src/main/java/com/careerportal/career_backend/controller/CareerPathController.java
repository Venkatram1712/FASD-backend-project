package com.careerportal.career_backend.controller;

import com.careerportal.career_backend.dto.CareerPathRequest;
import com.careerportal.career_backend.entity.CareerPath;
import com.careerportal.career_backend.service.CareerPathService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/career-paths", "/api/paths"})
@CrossOrigin(origins = "*")
public class CareerPathController {

    private final CareerPathService service;

    public CareerPathController(CareerPathService service) {
        this.service = service;
    }

    @GetMapping
    public List<CareerPath> getAll() {
        return service.getAll();
    }

    @PostMapping
    public ResponseEntity<CareerPath> create(@RequestBody CareerPathRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    public CareerPath update(@PathVariable Long id, @RequestBody CareerPathRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
