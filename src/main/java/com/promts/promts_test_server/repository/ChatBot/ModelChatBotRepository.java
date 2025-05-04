package com.promts.promts_test_server.repository.ChatBot;

import com.promts.promts_test_server.dto.ChatBot.inbound.CreateChatBotDTO;
import com.promts.promts_test_server.dto.ChatBot.outbound.ChatBotResponseDTO;
import com.promts.promts_test_server.dto.ChatBot.outbound.ChatBotsResponseDTO;

import java.util.List;

public class ModelChatBotRepository implements ChatBotRepository{
    @Override
    public List<ChatBotResponseDTO> getAllChatBotsBySpec(String uidFirebase, String search, int offset, int limit) {
        return null;
    }

    @Override
    public ChatBotResponseDTO getChatBotById(String uidFirebase, Long chatBotId) {
        return null;
    }

    @Override
    public ChatBotResponseDTO createChatBotById(String uidFirebase, CreateChatBotDTO createChatBotDTO) {
        return null;
    }
}
