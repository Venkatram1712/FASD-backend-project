package com.careerportal.career_backend.dto;

import java.time.LocalDateTime;
import java.util.List;

public class QuestionnaireResponseDTO {

    private Long id;
    private Long userId;
    private List<String> interests;
    private String strengths;
    private String careerGoals;
    private String educationLevel;
    private List<String> industries;
    private String workStyle;
    private String skills;
    private String timeline;
    private LocalDateTime submittedAt;
    private LocalDateTime createdAt;

    public QuestionnaireResponseDTO() {
    }

    public QuestionnaireResponseDTO(
            Long id,
            Long userId,
            List<String> interests,
            String strengths,
            String careerGoals,
            String educationLevel,
            List<String> industries,
            String workStyle,
            String skills,
            String timeline,
            LocalDateTime submittedAt,
            LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.interests = interests;
        this.strengths = strengths;
        this.careerGoals = careerGoals;
        this.educationLevel = educationLevel;
        this.industries = industries;
        this.workStyle = workStyle;
        this.skills = skills;
        this.timeline = timeline;
        this.submittedAt = submittedAt;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    public String getStrengths() {
        return strengths;
    }

    public void setStrengths(String strengths) {
        this.strengths = strengths;
    }

    public String getCareerGoals() {
        return careerGoals;
    }

    public void setCareerGoals(String careerGoals) {
        this.careerGoals = careerGoals;
    }

    public String getEducationLevel() {
        return educationLevel;
    }

    public void setEducationLevel(String educationLevel) {
        this.educationLevel = educationLevel;
    }

    public List<String> getIndustries() {
        return industries;
    }

    public void setIndustries(List<String> industries) {
        this.industries = industries;
    }

    public String getWorkStyle() {
        return workStyle;
    }

    public void setWorkStyle(String workStyle) {
        this.workStyle = workStyle;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getTimeline() {
        return timeline;
    }

    public void setTimeline(String timeline) {
        this.timeline = timeline;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
