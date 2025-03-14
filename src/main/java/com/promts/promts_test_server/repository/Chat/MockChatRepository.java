package com.promts.promts_test_server.repository.Chat;

import com.promts.promts_test_server.config.MockConfig;
import com.promts.promts_test_server.dto.Chat.inbound.ChatModelDTO;
import com.promts.promts_test_server.dto.Chat.inbound.CreateChatDTO;
import com.promts.promts_test_server.dto.Chat.inbound.CreateChatWithChatBotDTO;
import com.promts.promts_test_server.dto.Chat.inbound.UpdateChatSettingsDTO;
import com.promts.promts_test_server.dto.Chat.outbound.ChatListShortDTO;
import com.promts.promts_test_server.dto.Chat.outbound.CreatedChatDTO;
import com.promts.promts_test_server.dto.Chat.outbound.UpdatedChatSettingsDTO;
import com.promts.promts_test_server.repository.ChatBot.MockChatBotRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
@Profile("mock")
public class MockChatRepository implements ChatRepository{

    @Autowired
    private MockChatBotRepository mockChatBotRepository;
    @Autowired
    private MockConfig mockConfig;

    @Override
    public CreatedChatDTO createChat(String uidFirebase, Long id, CreateChatDTO createChatDTO) throws InterruptedException {

        // Текущая дата и время для примера
        LocalDateTime now = LocalDateTime.now();

        // Имитация ожидания запроса
        Thread.sleep(mockConfig.getDelay());

        mockResponseChats.add(new ChatModelDTO(
                (long)mockResponseChats.size()+1,
                id,
                null,
                "Чат номер " + (long)mockResponseChats.size()+1,
                createChatDTO.getModelUriId(),
                createChatDTO.getTemperature(),
                createChatDTO.getContext(),
                false,
                createChatDTO.isUseMemory(),
                createChatDTO.isUpdateMemory(),
                true,
                true,
                true,
                true,
                now,
                now
        ));

        return new CreatedChatDTO(
                (long)mockResponseChats.size(),
                null,
                "Чат номер " + (long)mockResponseChats.size(),
                createChatDTO.getModelUriId(),
                createChatDTO.getTemperature(),
                createChatDTO.getContext(),
                false,
                createChatDTO.isUseMemory(),
                createChatDTO.isUpdateMemory(),
                true,
                true,
                true,
                true,
                now,
                now
        );
    }

    @Override
    public CreatedChatDTO createChatWithChatBot(String uidFirebase, Long id, CreateChatWithChatBotDTO createChatWithChatBotDTO) throws InterruptedException {


        // Текущая дата и время для примера
        LocalDateTime now = LocalDateTime.now();

        // Имитация ожидания запроса
        Thread.sleep(mockConfig.getDelay());

        mockResponseChats.add(new ChatModelDTO(
                (long)mockResponseChats.size()+1,
                id,
                createChatWithChatBotDTO.getChatBotId(),
                "Чат номер " + (long)mockResponseChats.size()+1,
                createChatWithChatBotDTO.getModelUriId(),
                createChatWithChatBotDTO.getTemperature(),
                createChatWithChatBotDTO.getContext(),
                false,
                mockChatBotRepository.mockResponseChatBot.get(Math.toIntExact(createChatWithChatBotDTO.getChatBotId())-1).isCanUseMemory(),
                mockChatBotRepository.mockResponseChatBot.get(Math.toIntExact(createChatWithChatBotDTO.getChatBotId())-1).isCanUpdateMemory(),
                mockChatBotRepository.mockResponseChatBot.get(Math.toIntExact(createChatWithChatBotDTO.getChatBotId())-1).isCanUseMemory(),
                mockChatBotRepository.mockResponseChatBot.get(Math.toIntExact(createChatWithChatBotDTO.getChatBotId())-1).isCanUpdateMemory(),
                mockChatBotRepository.mockResponseChatBot.get(Math.toIntExact(createChatWithChatBotDTO.getChatBotId())-1).isCanEditModelUri(),
                mockChatBotRepository.mockResponseChatBot.get(Math.toIntExact(createChatWithChatBotDTO.getChatBotId())-1).isCanEditContext(),
                now,
                now
        ));

        return new CreatedChatDTO(
                (long)mockResponseChats.size(),
                createChatWithChatBotDTO.getChatBotId(),
                "Чат номер " + (long)mockResponseChats.size(),
                createChatWithChatBotDTO.getModelUriId(),
                createChatWithChatBotDTO.getTemperature(),
                createChatWithChatBotDTO.getContext(),
                false,
                mockChatBotRepository.mockResponseChatBot.get(Math.toIntExact(createChatWithChatBotDTO.getChatBotId())-1).isCanUseMemory(),
                mockChatBotRepository.mockResponseChatBot.get(Math.toIntExact(createChatWithChatBotDTO.getChatBotId())-1).isCanUpdateMemory(),
                mockChatBotRepository.mockResponseChatBot.get(Math.toIntExact(createChatWithChatBotDTO.getChatBotId())-1).isCanUseMemory(),
                mockChatBotRepository.mockResponseChatBot.get(Math.toIntExact(createChatWithChatBotDTO.getChatBotId())-1).isCanUpdateMemory(),
                mockChatBotRepository.mockResponseChatBot.get(Math.toIntExact(createChatWithChatBotDTO.getChatBotId())-1).isCanEditModelUri(),
                mockChatBotRepository.mockResponseChatBot.get(Math.toIntExact(createChatWithChatBotDTO.getChatBotId())-1).isCanEditContext(),
                now,
                now
        );
    }

    @Override
    public ChatListShortDTO getUserChats(String uidFirebase, Long id) throws InterruptedException {

        // Имитация ожидания запроса
        Thread.sleep(mockConfig.getDelay());

        ChatListShortDTO responseDTO = new ChatListShortDTO();
        List<ChatListShortDTO.ChatList> chatList = new ArrayList<>();

        for (ChatModelDTO mockResponseChat : mockResponseChats) {
            if (Objects.equals(mockResponseChat.getUserId(), id)) {
                chatList.add(new ChatListShortDTO.ChatList(
                        mockResponseChat.getId(),
                        mockResponseChat.getChatName(),
                        mockResponseChat.isStarredChat(),
                        mockResponseChat.getDateEdit()
                ));
            }
        }
        responseDTO.setChats(chatList);

        return responseDTO;
    }

    @Override
    public UpdatedChatSettingsDTO updateChatSettings(String uidFirebase, Long id, UpdateChatSettingsDTO updateChatSettingsDTO) throws InterruptedException {

        // Имитация ожидания запроса
        Thread.sleep(mockConfig.getDelay());

        mockResponseChats.get(Math.toIntExact(updateChatSettingsDTO.getChatId()-1)).setModelUriId(updateChatSettingsDTO.getModelUriId());
        mockResponseChats.get(Math.toIntExact(updateChatSettingsDTO.getChatId()-1)).setTemperature(updateChatSettingsDTO.getTemperature());
        mockResponseChats.get(Math.toIntExact(updateChatSettingsDTO.getChatId()-1)).setContext(updateChatSettingsDTO.getContext());
        mockResponseChats.get(Math.toIntExact(updateChatSettingsDTO.getChatId()-1)).setUseMemory(updateChatSettingsDTO.isUseMemory());
        mockResponseChats.get(Math.toIntExact(updateChatSettingsDTO.getChatId()-1)).setUpdateMemory(updateChatSettingsDTO.isUpdateMemory());

        return new UpdatedChatSettingsDTO(
                updateChatSettingsDTO.getChatId(),
                updateChatSettingsDTO.getModelUriId(),
                updateChatSettingsDTO.getTemperature(),
                updateChatSettingsDTO.getContext(),
                updateChatSettingsDTO.isUseMemory(),
                updateChatSettingsDTO.isUpdateMemory()
        );
    }

    // Контейнер для хранения моков чатов
    public final List<ChatModelDTO> mockResponseChats = new ArrayList<>();

    @PostConstruct
    private void initMockResponseChats() {
        // Текущая дата и время для примера
        LocalDateTime now = LocalDateTime.now();

        mockResponseChats.add(new ChatModelDTO(
                1L,
                2L,
                null,
                "Обычный чат 1",
                1L,
                0.7,
                "Контекст для обычного чата 1",
                false,
                true,
                false,
                true,
                true,
                true,
                true,
                now.minusMinutes(10),
                now.minusHours(1)
        ));

        mockResponseChats.add(new ChatModelDTO(
                2L,
                2L,
                1L,
                "Чат с ботом",
                2L,
                0.5,
                "Ты помощник по программированию",
                true,
                true,
                true,
                true,
                true,
                true,
                true,
                now.minusMinutes(20),
                now.minusHours(2)
        ));

        mockResponseChats.add(new ChatModelDTO(
                3L,
                2L,
                null,
                "Чат обновления памяти",
                1L,
                0.8,
                "Контекст для чата обновления памяти",
                false,
                true,
                true,
                true,
                true,
                true,
                true,
                now.minusMinutes(30),
                now.minusHours(3)
        ));

        mockResponseChats.add(new ChatModelDTO(
                4L,
                2L,
                null,
                "Закрепленный чат",
                3L,
                0.6,
                "Контекст закрепленного чата",
                true,
                true,
                true,
                true,
                true,
                true,
                true,
                now.minusMinutes(5),
                now.minusHours(4)
        ));

        mockResponseChats.add(new ChatModelDTO(
                5L,
                2L,
                null,
                "Чат с редактированием контекста",
                2L,
                0.9,
                "Первоначальный контекст",
                false,
                true,
                true,
                true,
                true,
                true,
                true,
                now.minusMinutes(15),
                now.minusHours(5)
        ));
    }

}
