package com.careerportal.career_backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

	@Column(unique = true, nullable = false)
    private String email;

    private String password;

	@Column(name = "phone")
	private String phone;

	@Column(name = "bio", length = 2000)
	private String bio;

	@Column(name = "institution")
	private String institution;

	@Column(name = "specialization")
	private String specialization;

	@Column(name = "experience_years")
	private Integer experienceYears;

	@Column(name = "questionnaire_completed", nullable = false)
	private Boolean questionnaireCompleted = false;

	@Column(name = "status", nullable = false)
	private String status = "active";

	@Column(nullable = false)
	private String provider = "local";

	@Column(unique = true)
	private String googleSub;

	@Column(name = "assigned_counselor_id")
	private Long assignedCounselorId;

    private String role; // STUDENT / ADMIN / COUNSELOR

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public String getInstitution() {
		return institution;
	}

	public void setInstitution(String institution) {
		this.institution = institution;
	}

	public String getSpecialization() {
		return specialization;
	}

	public void setSpecialization(String specialization) {
		this.specialization = specialization;
	}

	public Integer getExperienceYears() {
		return experienceYears;
	}

	public void setExperienceYears(Integer experienceYears) {
		this.experienceYears = experienceYears;
	}

	public Boolean getQuestionnaireCompleted() {
		return questionnaireCompleted;
	}

	public void setQuestionnaireCompleted(Boolean questionnaireCompleted) {
		this.questionnaireCompleted = questionnaireCompleted;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getGoogleSub() {
		return googleSub;
	}

	public void setGoogleSub(String googleSub) {
		this.googleSub = googleSub;
	}

	public Long getAssignedCounselorId() {
		return assignedCounselorId;
	}

	public void setAssignedCounselorId(Long assignedCounselorId) {
		this.assignedCounselorId = assignedCounselorId;
	}
}
