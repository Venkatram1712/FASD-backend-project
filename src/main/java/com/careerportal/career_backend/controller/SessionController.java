package com.careerportal.career_backend.controller;

import com.careerportal.career_backend.dto.ErrorResponse;
import com.careerportal.career_backend.dto.SessionDTO;
import com.careerportal.career_backend.dto.SuccessResponse;
import com.careerportal.career_backend.entity.Session;
import com.careerportal.career_backend.repository.UserRepository;
import com.careerportal.career_backend.service.SessionService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {

    private final SessionService sessionService;
    private final UserRepository userRepository;

    public SessionController(SessionService sessionService, UserRepository userRepository) {
        this.sessionService = sessionService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<?> createSession(@RequestBody SessionDTO dto) {
        try {
            Session saved = sessionService.createSession(dto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new SuccessResponse("SESSION_CREATED", "Session created successfully", toDTO(saved)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("INVALID_INPUT", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ErrorResponse("CREATE_FAILED", e.getMessage()));
        }
    }

    @GetMapping("/counselor/{counselorId}")
    public ResponseEntity<?> getByCounselor(@PathVariable Long counselorId) {
        try {
            List<SessionDTO> list = sessionService.byCounselor(counselorId)
                    .stream().map(this::toDTO).collect(Collectors.toList());
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ErrorResponse("FETCH_FAILED", e.getMessage()));
        }
    }

    @GetMapping("/student/{studentId}")
        public ResponseEntity<?> getByStudent(@PathVariable Long studentId) {
        try {
            List<SessionDTO> list = sessionService.byStudent(studentId)
                .stream().map(this::toDTO).collect(Collectors.toList());
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(new ErrorResponse("FETCH_FAILED", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(toDTO(sessionService.byId(id)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody SessionDTO dto) {
        try {
            Session updated = sessionService.updateSession(id, dto);
            return ResponseEntity.ok(new SuccessResponse("SESSION_UPDATED", "Session updated", toDTO(updated)));
        } catch (IllegalArgumentException e) {
            if (e.getMessage() != null && e.getMessage().startsWith("Session not found")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().body(new ErrorResponse("INVALID_INPUT", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ErrorResponse("UPDATE_FAILED", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            sessionService.deleteSession(id);
            return ResponseEntity.ok(new SuccessResponse(
                    "SESSION_DELETED",
                    "Session deleted successfully",
                    id
            ));
        } catch (IllegalArgumentException e) {
            if (e.getMessage() != null && e.getMessage().startsWith("Session not found")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().body(new ErrorResponse("INVALID_INPUT", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ErrorResponse("DELETE_FAILED", e.getMessage()));
        }
    }

    private SessionDTO toDTO(Session session) {
        SessionDTO dto = new SessionDTO();
        dto.setId(session.getId());
        dto.setCounselorId(session.getCounselorId());
        dto.setStudentId(session.getStudentId());
        dto.setTopic(session.getTopic());
        dto.setDate(session.getDate());
        dto.setTime(session.getTime());
        dto.setStartTime(session.getStartTime() == null ? null : session.getStartTime().toString());
        dto.setEndTime(session.getEndTime() == null ? null : session.getEndTime().toString());
        dto.setStatus(session.getStatus());
        dto.setNotes(session.getNotes());
        dto.setCreatedAt(session.getCreatedAt());
        dto.setUpdatedAt(session.getUpdatedAt());

        if (session.getCounselorId() != null) {
            userRepository.findById(session.getCounselorId()).ifPresent(u -> dto.setCounselorName(u.getName()));
        }
        if (session.getStudentId() != null) {
            userRepository.findById(session.getStudentId()).ifPresent(u -> dto.setStudentName(u.getName()));
        }
        return dto;
    }
}
