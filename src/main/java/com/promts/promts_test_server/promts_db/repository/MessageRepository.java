package com.promts.promts_test_server.promts_db.repository;

import com.promts.promts_test_server.promts_db.entity.message.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByChatIdAndOldMessageFalseOrderByDateCreate(Long chatId);
}
