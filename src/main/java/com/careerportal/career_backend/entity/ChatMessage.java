package com.careerportal.career_backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "chat_messages")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "session_id", nullable = false)
    private Long sessionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", insertable = false, updatable = false,
                foreignKey = @jakarta.persistence.ForeignKey(name = "none", value = jakarta.persistence.ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Session session;

    @Column(name = "sender_id", nullable = false)
    private Long senderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", insertable = false, updatable = false,
                foreignKey = @jakarta.persistence.ForeignKey(name = "none", value = jakarta.persistence.ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User sender;

    @Column(length = 50)
    private String senderRole;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String message;

    @Column(length = 50)
    private String messageType;

    @Column(length = 500)
    private String fileUrl;

    @Column(nullable = false, updatable = false)
    private LocalDateTime sentAt;

    @Column
    private LocalDateTime editedAt;

    @Column
    private Boolean isRead = false;

    @PrePersist
    protected void onCreate() {
        sentAt = LocalDateTime.now();
        if (messageType == null) {
            messageType = "text";
        }
        if (isRead == null) {
            isRead = false;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public String getSenderRole() {
        return senderRole;
    }

    public void setSenderRole(String senderRole) {
        this.senderRole = senderRole;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public LocalDateTime getEditedAt() {
        return editedAt;
    }

    public void setEditedAt(LocalDateTime editedAt) {
        this.editedAt = editedAt;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }
}
