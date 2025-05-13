package com.promts.promts_test_server.promts_db.repository;

import com.promts.promts_test_server.promts_db.entity.chat.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findByUserId(Long userId);
}
