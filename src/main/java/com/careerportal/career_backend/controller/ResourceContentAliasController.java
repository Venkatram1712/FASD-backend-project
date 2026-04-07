package com.careerportal.career_backend.controller;

import com.careerportal.career_backend.entity.ResourceContent;
import com.careerportal.career_backend.service.CareerResourceService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/resource-contents")
public class ResourceContentAliasController {

    private final CareerResourceService service;

    public ResourceContentAliasController(CareerResourceService service) {
        this.service = service;
    }

    @GetMapping("/resource/{resourceId}")
    public List<ResourceContent> getContentsByResource(@PathVariable Long resourceId) {
        return service.getContents(resourceId);
    }
}
