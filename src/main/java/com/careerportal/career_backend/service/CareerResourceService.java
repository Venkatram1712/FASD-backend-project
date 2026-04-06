package com.careerportal.career_backend.service;

import com.careerportal.career_backend.dto.CareerResourceRequest;
import com.careerportal.career_backend.dto.ResourceContentRequest;
import com.careerportal.career_backend.entity.CareerResource;
import com.careerportal.career_backend.entity.ResourceContent;
import com.careerportal.career_backend.repository.CareerResourceRepository;
import com.careerportal.career_backend.repository.ResourceContentRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CareerResourceService {

    private final CareerResourceRepository resourceRepository;
    private final ResourceContentRepository contentRepository;

    public CareerResourceService(CareerResourceRepository resourceRepository,
            ResourceContentRepository contentRepository) {
        this.resourceRepository = resourceRepository;
        this.contentRepository = contentRepository;
    }

    public List<CareerResource> getAllResources() {
        return resourceRepository.findAll();
    }

    public CareerResource createResource(CareerResourceRequest request) {
        CareerResource resource = new CareerResource();
        resource.setTitle(request.getTitle() == null ? "" : request.getTitle().trim());
        resource.setCategory(request.getCategory() == null ? "General" : request.getCategory().trim());
        return resourceRepository.save(resource);
    }

    public CareerResource updateResource(Long id, CareerResourceRequest request) {
        CareerResource existing = resourceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Resource not found: " + id));

        if (request.getTitle() != null) {
            existing.setTitle(request.getTitle().trim());
        }
        if (request.getCategory() != null) {
            existing.setCategory(request.getCategory().trim());
        }

        return resourceRepository.save(existing);
    }

    public void deleteResource(Long id) {
        List<ResourceContent> contents = contentRepository.findByResourceIdOrderByIdAsc(id);
        contentRepository.deleteAll(contents);
        resourceRepository.deleteById(id);
    }

    public List<ResourceContent> getContents(Long resourceId) {
        return contentRepository.findByResourceIdOrderByIdAsc(resourceId);
    }

    public ResourceContent createContent(Long resourceId, ResourceContentRequest request) {
        CareerResource resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> new IllegalArgumentException("Resource not found: " + resourceId));

        ResourceContent content = new ResourceContent();
        content.setResource(resource);
        content.setType(normalizeType(request.getType()));
        content.setContentText(trimToNull(request.getContentText()));
        content.setVideoUrl(trimToNull(request.getVideoUrl()));
        return contentRepository.save(content);
    }

    public ResourceContent updateContent(Long resourceId, Long contentId, ResourceContentRequest request) {
        ResourceContent existing = contentRepository.findById(contentId)
                .orElseThrow(() -> new IllegalArgumentException("Content not found: " + contentId));

        if (!existing.getResource().getId().equals(resourceId)) {
            throw new IllegalArgumentException("Content does not belong to resource " + resourceId);
        }

        if (request.getType() != null) {
            existing.setType(normalizeType(request.getType()));
        }
        if (request.getContentText() != null) {
            existing.setContentText(trimToNull(request.getContentText()));
        }
        if (request.getVideoUrl() != null) {
            existing.setVideoUrl(trimToNull(request.getVideoUrl()));
        }

        return contentRepository.save(existing);
    }

    public void deleteContent(Long resourceId, Long contentId) {
        ResourceContent existing = contentRepository.findById(contentId)
                .orElseThrow(() -> new IllegalArgumentException("Content not found: " + contentId));

        if (!existing.getResource().getId().equals(resourceId)) {
            throw new IllegalArgumentException("Content does not belong to resource " + resourceId);
        }

        contentRepository.delete(existing);
    }

    private String normalizeType(String type) {
        String value = type == null ? "text" : type.trim().toLowerCase();
        return "video".equals(value) ? "video" : "text";
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
