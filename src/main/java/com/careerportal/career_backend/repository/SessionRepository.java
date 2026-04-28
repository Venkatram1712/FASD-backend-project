package com.careerportal.career_backend.repository;

import com.careerportal.career_backend.entity.Session;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

    List<Session> findByCounselorId(Long counselorId);

    List<Session> findByCounselorIdOrderByDateAsc(Long counselorId);

    List<Session> findByStudentId(Long studentId);

    List<Session> findByStudentIdOrderByDateAsc(Long studentId);

    @Query("SELECT s FROM Session s WHERE s.counselorId = :counselorId AND s.date >= :now AND s.status = 'scheduled' ORDER BY s.date ASC")
    List<Session> findUpcomingSessionsByCounselor(@Param("counselorId") Long counselorId, @Param("now") LocalDateTime now);

    List<Session> findByCounselorIdAndStudentId(Long counselorId, Long studentId);

    @Query("SELECT s FROM Session s WHERE s.counselorId = :counselorId AND s.status = 'completed' ORDER BY s.date DESC")
    List<Session> findCompletedSessionsByCounselor(@Param("counselorId") Long counselorId);
}
