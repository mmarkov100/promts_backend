package com.promts.promts_test_server.repository.ChatBot;

import com.promts.promts_test_server.dto.ChatBot.inbound.CreateChatBotDTO;
import com.promts.promts_test_server.dto.ChatBot.outbound.ChatBotResponseDTO;
import com.promts.promts_test_server.dto.ChatBot.outbound.ChatBotsResponseDTO;

public class ModelChatBotRepository implements ChatBotRepository{
    @Override
    public ChatBotsResponseDTO getAllChatBotsBySpec(Long id, String uidFirebase, String search, int offset, int limit) {
        return null;
    }

    @Override
    public ChatBotResponseDTO getChatBotById(Long id, String uidFirebase, Long chatBotId) {
        return null;
    }

    @Override
    public ChatBotResponseDTO createChatBotById(Long id, String uidFirebase, CreateChatBotDTO createChatBotDTO) {
        return null;
    }
}
