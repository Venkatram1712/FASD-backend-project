package com.careerportal.career_backend.controller;

import com.careerportal.career_backend.dto.ErrorResponse;
import com.careerportal.career_backend.dto.SuccessResponse;
import com.careerportal.career_backend.entity.ChatMessage;
import com.careerportal.career_backend.entity.Session;
import com.careerportal.career_backend.repository.ChatMessageRepository;
import com.careerportal.career_backend.service.SessionService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sessions")
@CrossOrigin(origins = "*")
public class ChatMessageController {

    private final ChatMessageRepository chatMessageRepository;
    private final SessionService sessionService;

    public ChatMessageController(ChatMessageRepository chatMessageRepository, SessionService sessionService) {
        this.chatMessageRepository = chatMessageRepository;
        this.sessionService = sessionService;
    }

    @PostMapping("/{sessionId}/messages")
    public ResponseEntity<?> sendMessage(@PathVariable Long sessionId, @RequestBody ChatMessage body) {
        try {
            Session session = sessionService.byId(sessionId);

            if (!sessionService.isChatWindowOpen(session)) {
                return ResponseEntity.badRequest().body(new ErrorResponse(
                        "CHAT_WINDOW_CLOSED",
                        "Chat is allowed only during session window: "
                                + session.getStartTime() + " - " + session.getEndTime()));
            }

            String status = session.getStatus() == null ? "" : session.getStatus().toLowerCase();
            if (!(status.equals("scheduled") || status.equals("accepted") || status.equals("active"))) {
                return ResponseEntity.badRequest().body(new ErrorResponse(
                        "CHAT_NOT_ALLOWED",
                        "Chat is not enabled for current session status: " + status));
            }

            if (body.getSenderId() == null) {
                return ResponseEntity.badRequest().body(new ErrorResponse("INVALID_INPUT", "senderId is required"));
            }
            if (body.getMessage() == null || body.getMessage().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(new ErrorResponse("INVALID_INPUT", "message is required"));
            }

            ChatMessage m = new ChatMessage();
            m.setSessionId(sessionId);
            m.setSenderId(body.getSenderId());
            m.setSenderRole(body.getSenderRole());
            m.setMessage(body.getMessage());
            m.setSentAt(LocalDateTime.now());
            m.setIsRead(false);

            ChatMessage saved = chatMessageRepository.save(m);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new SuccessResponse("MESSAGE_SENT", "Message sent", saved));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("INVALID_INPUT", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ErrorResponse("SEND_FAILED", e.getMessage()));
        }
    }

    @GetMapping("/{sessionId}/messages")
    public ResponseEntity<?> getMessages(@PathVariable Long sessionId) {
        try {
            List<ChatMessage> list = chatMessageRepository.findBySessionId(sessionId);
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ErrorResponse("FETCH_FAILED", e.getMessage()));
        }
    }
}
