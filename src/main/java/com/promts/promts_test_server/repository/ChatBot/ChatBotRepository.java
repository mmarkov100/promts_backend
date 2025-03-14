package com.promts.promts_test_server.repository.ChatBot;

import com.promts.promts_test_server.dto.ChatBot.inbound.CreateChatBotDTO;
import com.promts.promts_test_server.dto.ChatBot.outbound.ChatBotResponseDTO;
import com.promts.promts_test_server.dto.ChatBot.outbound.ChatBotsResponseDTO;

public interface ChatBotRepository {
    ChatBotsResponseDTO getAllChatBotsBySpec(Long id, String uidFirebase, String search, int offset, int limit) throws InterruptedException;
    ChatBotResponseDTO getChatBotById(Long id, String uidFirebase, Long chatBotId) throws InterruptedException;
    ChatBotResponseDTO createChatBotById(Long id, String uidFirebase, CreateChatBotDTO createChatBotDTO) throws InterruptedException;
}
