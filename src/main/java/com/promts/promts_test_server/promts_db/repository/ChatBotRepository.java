package com.promts.promts_test_server.promts_db.repository;

import com.promts.promts_test_server.promts_db.entity.chatbot.ChatBot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatBotRepository extends JpaRepository<ChatBot, Long> {

}
