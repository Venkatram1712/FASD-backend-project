package com.careerportal.career_backend.service;

import com.careerportal.career_backend.dto.SessionDTO;
import com.careerportal.career_backend.entity.Session;
import com.careerportal.career_backend.repository.SessionRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SessionService {

    private static final DateTimeFormatter HH_MM = DateTimeFormatter.ofPattern("HH:mm");

    private final SessionRepository sessionRepository;

    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public Session createSession(SessionDTO dto) {
        if (dto.getCounselorId() == null) {
            throw new IllegalArgumentException("counselorId is required");
        }
        if (dto.getStudentId() == null) {
            throw new IllegalArgumentException("studentId is required");
        }
        if (dto.getDate() == null) {
            throw new IllegalArgumentException("date is required in format yyyy-MM-ddTHH:mm:ss");
        }

        LocalTime startTime = resolveStartTime(dto);
        LocalTime endTime = resolveEndTime(dto, startTime);
        validateTimeRange(startTime, endTime);

        Session session = new Session();
        session.setCounselorId(dto.getCounselorId());
        session.setStudentId(dto.getStudentId());
        session.setTopic(dto.getTopic());
        session.setDate(dto.getDate());
        session.setTime(dto.getTime());
        session.setStartTime(startTime);
        session.setEndTime(endTime);
        session.setNotes(dto.getNotes());
        session.setStatus("scheduled");

        return sessionRepository.save(session);
    }

    public Session updateSession(Long id, SessionDTO dto) {
        Session session = byId(id);

        if (dto.getTopic() != null) {
            session.setTopic(dto.getTopic());
        }
        if (dto.getDate() != null) {
            session.setDate(dto.getDate());
        }
        if (dto.getTime() != null) {
            session.setTime(dto.getTime());
        }
        if (dto.getStartTime() != null || dto.getTime() != null) {
            session.setStartTime(resolveStartTime(dto));
        }
        if (dto.getEndTime() != null) {
            session.setEndTime(parseTime(dto.getEndTime(), "endTime"));
        }
        if (dto.getNotes() != null) {
            session.setNotes(dto.getNotes());
        }
        if (dto.getStatus() != null) {
            String normalizedStatus = normalizeStatus(dto.getStatus(), session.getStatus());
            if ("requested".equals(normalizedStatus)) {
                throw new IllegalArgumentException("requested status is not allowed");
            }
            session.setStatus(normalizedStatus);
        }

        LocalTime startTime = session.getStartTime();
        LocalTime endTime = session.getEndTime();
        if (startTime == null) {
            startTime = fallbackStartFromDateOrLegacyTime(session.getDate(), session.getTime());
            session.setStartTime(startTime);
        }
        if (endTime == null) {
            endTime = startTime.plusHours(1);
            session.setEndTime(endTime);
        }
        validateTimeRange(startTime, endTime);

        return sessionRepository.save(session);
    }

    @Transactional(readOnly = true)
    public List<Session> byCounselor(Long counselorId) {
        return sessionRepository.findByCounselorIdOrderByDateAsc(counselorId);
    }

    @Transactional(readOnly = true)
    public List<Session> byStudent(Long studentId) {
        return sessionRepository.findByStudentIdOrderByDateAsc(studentId);
    }

    @Transactional(readOnly = true)
    public Session byId(Long id) {
        return sessionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Session not found: " + id));
    }

    public void deleteSession(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Session id is required");
        }

        Session existing = sessionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Session not found: " + id));
        sessionRepository.delete(existing);
    }

    @Transactional(readOnly = true)
    public boolean isChatWindowOpen(Session session) {
        LocalDate date = session.getDate().toLocalDate();
        LocalTime startTime = session.getStartTime();
        LocalTime endTime = session.getEndTime();

        if (startTime == null) {
            startTime = fallbackStartFromDateOrLegacyTime(session.getDate(), session.getTime());
        }
        if (endTime == null) {
            endTime = startTime.plusHours(1);
        }

        LocalDateTime start = LocalDateTime.of(date, startTime);
        LocalDateTime end = LocalDateTime.of(date, endTime);
        LocalDateTime now = LocalDateTime.now();
        return !now.isBefore(start) && !now.isAfter(end);
    }

    private void validateTimeRange(LocalTime startTime, LocalTime endTime) {
        if (startTime == null || endTime == null) {
            throw new IllegalArgumentException("startTime and endTime are required");
        }
        if (!endTime.isAfter(startTime)) {
            throw new IllegalArgumentException("endTime must be after startTime");
        }
    }

    private LocalTime resolveStartTime(SessionDTO dto) {
        if (dto.getStartTime() != null && !dto.getStartTime().isBlank()) {
            return parseTime(dto.getStartTime(), "startTime");
        }
        return fallbackStartFromDateOrLegacyTime(dto.getDate(), dto.getTime());
    }

    private LocalTime resolveEndTime(SessionDTO dto, LocalTime startTime) {
        if (dto.getEndTime() != null && !dto.getEndTime().isBlank()) {
            return parseTime(dto.getEndTime(), "endTime");
        }
        return startTime.plusHours(1);
    }

    private LocalTime fallbackStartFromDateOrLegacyTime(LocalDateTime date, String legacyTime) {
        if (legacyTime != null && !legacyTime.isBlank()) {
            return parseTime(legacyTime, "time");
        }
        if (date == null) {
            throw new IllegalArgumentException("date is required to derive startTime");
        }
        return date.toLocalTime().withSecond(0).withNano(0);
    }

    private LocalTime parseTime(String value, String fieldName) {
        try {
            return LocalTime.parse(value.trim(), HH_MM);
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException(fieldName + " must be in HH:mm format");
        }
    }

    private String normalizeStatus(String status, String fallback) {
        if (status == null || status.isBlank()) {
            return fallback == null || fallback.isBlank() ? "scheduled" : fallback.toLowerCase();
        }
        return status.trim().toLowerCase();
    }
}
