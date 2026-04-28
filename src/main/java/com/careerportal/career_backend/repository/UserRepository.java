package com.careerportal.career_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.careerportal.career_backend.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailIgnoreCase(String email);

    Optional<User> findByGoogleSub(String googleSub);

    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);

    java.util.List<User> findByRole(String role);

    java.util.List<User> findByRoleIgnoreCase(String role);
}
