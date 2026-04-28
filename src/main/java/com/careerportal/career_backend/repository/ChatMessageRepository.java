package com.careerportal.career_backend.repository;

import com.careerportal.career_backend.entity.ChatMessage;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    @Query("SELECT m FROM ChatMessage m WHERE m.sessionId = :sessionId ORDER BY m.sentAt ASC")
    List<ChatMessage> findBySessionId(@Param("sessionId") Long sessionId);

    @Query("SELECT m FROM ChatMessage m WHERE m.sessionId = :sessionId AND m.senderId <> :userId AND m.isRead = false")
    List<ChatMessage> findUnreadBySessionId(@Param("sessionId") Long sessionId, @Param("userId") Long userId);

    @Query("SELECT m FROM ChatMessage m WHERE m.sessionId = :sessionId ORDER BY m.sentAt DESC")
    List<ChatMessage> findLatestBySessionId(@Param("sessionId") Long sessionId, org.springframework.data.domain.Pageable pageable);

    default List<ChatMessage> findLatestBySessionId(Long sessionId, int limit) {
        return findLatestBySessionId(sessionId, PageRequest.of(0, Math.max(1, limit)));
    }
}
