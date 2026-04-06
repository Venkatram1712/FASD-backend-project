package com.careerportal.career_backend.controller;

import com.careerportal.career_backend.dto.CareerResourceRequest;
import com.careerportal.career_backend.dto.ResourceContentRequest;
import com.careerportal.career_backend.entity.CareerResource;
import com.careerportal.career_backend.entity.ResourceContent;
import com.careerportal.career_backend.service.CareerResourceService;
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
@RequestMapping({"/api/resources", "/api/career-resources"})
@CrossOrigin(origins = "*")
public class CareerResourceController {

    private final CareerResourceService service;

    public CareerResourceController(CareerResourceService service) {
        this.service = service;
    }

    @GetMapping
    public List<CareerResource> getAllResources() {
        return service.getAllResources();
    }

    @PostMapping
    public ResponseEntity<CareerResource> createResource(@RequestBody CareerResourceRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createResource(request));
    }

    @PutMapping("/{id}")
    public CareerResource updateResource(@PathVariable Long id, @RequestBody CareerResourceRequest request) {
        return service.updateResource(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResource(@PathVariable Long id) {
        service.deleteResource(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{resourceId}/contents")
    public List<ResourceContent> getContents(@PathVariable Long resourceId) {
        return service.getContents(resourceId);
    }

    @PostMapping("/{resourceId}/contents")
    public ResponseEntity<ResourceContent> createContent(
            @PathVariable Long resourceId,
            @RequestBody ResourceContentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createContent(resourceId, request));
    }

    @PutMapping("/{resourceId}/contents/{contentId}")
    public ResourceContent updateContent(
            @PathVariable Long resourceId,
            @PathVariable Long contentId,
            @RequestBody ResourceContentRequest request) {
        return service.updateContent(resourceId, contentId, request);
    }

    @DeleteMapping("/{resourceId}/contents/{contentId}")
    public ResponseEntity<Void> deleteContent(
            @PathVariable Long resourceId,
            @PathVariable Long contentId) {
        service.deleteContent(resourceId, contentId);
        return ResponseEntity.noContent().build();
    }
}
