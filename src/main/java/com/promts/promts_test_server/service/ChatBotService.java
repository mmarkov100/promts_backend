package com.promts.promts_test_server.service;

import com.promts.promts_test_server.dto.ChatBot.inbound.CreateChatBotDTO;
import com.promts.promts_test_server.dto.ChatBot.outbound.ChatBotResponseDTO;
import com.promts.promts_test_server.exception.GlobalException;
import com.promts.promts_test_server.repository.ChatBot.ChatBotRepository;
import com.promts.promts_test_server.repository.NeuralNetwork.MockNeuralNetworkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ChatBotService {

    @Autowired
    private MockNeuralNetworkRepository mockNeuralNetworkRepository;
    @Autowired
    private ChatBotRepository chatBotRepository;

    // Метод для получения всех чат-ботов по определенным параметрам
    public List<ChatBotResponseDTO> getChatBots(String search, int limit, int offset, String uidFirebase) {
        try {
            return chatBotRepository.getAllChatBotsBySpec(uidFirebase, search, offset, limit);
        } catch (Exception e){
            throw new GlobalException("SERVICE_IS_NOT_ACTIVE", "Сервис недоступен");
        }
    }

    // Метод получения конкретного чат-бота
    public ChatBotResponseDTO getChatBot(Long chatBotId, String uidFirebase) {
        try {
            return chatBotRepository.getChatBotById(uidFirebase, chatBotId);
        } catch (Exception e){
            throw new GlobalException("SERVICE_IS_NOT_ACTIVE", "Сервис недоступен");
        }
    }

    public ChatBotResponseDTO createChatBot(CreateChatBotDTO createChatBotDTO, String uidFirebase) {
        try {
            return chatBotRepository.createChatBotById(uidFirebase, createChatBotDTO);
        } catch (Exception e){
            throw new GlobalException("SERVICE_IS_NOT_ACTIVE", "Сервис недоступен");
        }
    }
}
