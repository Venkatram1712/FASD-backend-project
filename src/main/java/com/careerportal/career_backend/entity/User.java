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

	@Column(nullable = false)
	private String provider = "local";

	@Column(unique = true)
	private String googleSub;

    private String role; // STUDENT / ADMIN / COUNSELOR

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
}
