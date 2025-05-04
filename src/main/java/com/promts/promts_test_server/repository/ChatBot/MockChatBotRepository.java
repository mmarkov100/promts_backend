package com.promts.promts_test_server.repository.ChatBot;

import com.promts.promts_test_server.config.MockConfig;
import com.promts.promts_test_server.dto.ChatBot.inbound.CreateChatBotDTO;
import com.promts.promts_test_server.dto.ChatBot.outbound.ChatBotResponseDTO;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@Profile("mock")
public class MockChatBotRepository implements ChatBotRepository {

    @Autowired
    private MockConfig mockConfig;

    @Override
    public List<ChatBotResponseDTO> getAllChatBotsBySpec(String uidFirebase, String search, int offset, int limit) throws InterruptedException {

        // Имитация ожидания запроса
        Thread.sleep(mockConfig.getDelay());

        return new ArrayList<>(mockResponseChatBot);
    }

    @Override
    public ChatBotResponseDTO getChatBotById(String uidFirebase, Long chatBotId) throws InterruptedException {

        // Имитация ожидания запроса
        Thread.sleep(mockConfig.getDelay());

        return mockResponseChatBot.getFirst();
    }

    @Override
    public ChatBotResponseDTO createChatBotById(String uidFirebase, CreateChatBotDTO createChatBotDTO) throws InterruptedException {

        // Имитация ожидания запроса
        Thread.sleep(mockConfig.getDelay());

        ChatBotResponseDTO newChatBot = new ChatBotResponseDTO(
                (long) (mockResponseChatBot.size()+1),
                createChatBotDTO.getChatBotName(),
                createChatBotDTO.getChatBotDesc(),
                createChatBotDTO.getHelloMessage(),
                createChatBotDTO.getModelUriId(),
                createChatBotDTO.getTemperature(),
                createChatBotDTO.getContext(),
                false,
                createChatBotDTO.isCanUseMemory(),
                createChatBotDTO.isCanUpdateMemory(),
                createChatBotDTO.isCanEditModelUri(),
                createChatBotDTO.isCanEditContext(),
                0,
                0
        );

    mockResponseChatBot.add(newChatBot);
        return newChatBot;
    }


    // Контейнер для хранения моков чат-ботов
    public final List<ChatBotResponseDTO> mockResponseChatBot = new ArrayList<>();

    @PostConstruct
    private void initMockResponseChatBot(){
        mockResponseChatBot.add(new ChatBotResponseDTO(
                1L,
                "Помощник по программированию",
                "Помогает с вопросами по Python, Java и другим языкам.",
                "Что будем считать?",
                1L,
                0.5,
                "Ты помощник по программированию",
                true,
                true,
                true,
                true,
                true,
                123,
                100));
        mockResponseChatBot.add(new ChatBotResponseDTO(
                2L,
                "Финансовый советник",
                "Помогает с вопросами по инвестициям и бюджету.",
                "Что будем считать?",
                2L,
                0.7,
                "Ты помощник по финансам",
                false,
                true,
                false,
                true,
                true,
                87,
                599));
    }
}
