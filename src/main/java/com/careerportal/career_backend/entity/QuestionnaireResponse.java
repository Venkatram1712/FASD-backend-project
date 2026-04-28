package com.careerportal.career_backend.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "questionnaire_responses")
public class QuestionnaireResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @ElementCollection
    @CollectionTable(name = "questionnaire_interests", joinColumns = @JoinColumn(name = "response_id"))
    @Column(name = "interest")
    private List<String> interests;

    @Column(columnDefinition = "TEXT")
    private String strengths;

    @Column(columnDefinition = "TEXT")
    private String careerGoals;

    @Column(length = 100)
    private String educationLevel;

    @ElementCollection
    @CollectionTable(name = "questionnaire_industries", joinColumns = @JoinColumn(name = "response_id"))
    @Column(name = "industry")
    private List<String> industries;

    @Column(length = 100)
    private String workStyle;

    @Column(columnDefinition = "TEXT")
    private String skills;

    @Column(length = 100)
    private String timeline;

    @Column(nullable = false)
    private LocalDateTime submittedAt;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (submittedAt == null) {
            submittedAt = createdAt;
        }
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
