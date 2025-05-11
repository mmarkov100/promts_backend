package com.promts.promts_test_server.promts_backend.repository.Chat;

import com.promts.promts_test_server.promts_backend.config.MockBackendConfig;
import com.promts.promts_test_server.promts_backend.dto.Chat.inbound.ChatModelDTO;
import com.promts.promts_test_server.promts_backend.dto.Chat.inbound.CreateChatDTO;
import com.promts.promts_test_server.promts_backend.dto.Chat.inbound.CreateChatWithChatBotDTO;
import com.promts.promts_test_server.promts_backend.dto.Chat.inbound.UpdateChatSettingsDTO;
import com.promts.promts_test_server.promts_backend.dto.Shared.outbound.SuccessMessageDTO;
import com.promts.promts_test_server.shared.exception.GlobalException;
import com.promts.promts_test_server.promts_backend.repository.ChatBot.MockChatBotRepository;
import com.promts.promts_test_server.promts_backend.repository.DataSore.MockDataStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

@Repository
@Profile({"mock", "neuro", "dbmock"})
public class MockChatRepository implements ChatRepository{

    private final MockChatBotRepository mockChatBotRepository;
    private final MockBackendConfig mockBackendConfig;
    private final MockDataStore mockDataStore;
    @Autowired
    public MockChatRepository(MockChatBotRepository mockChatBotRepository, MockBackendConfig mockBackendConfig, MockDataStore mockDataStore) {
        this.mockChatBotRepository = mockChatBotRepository;
        this.mockBackendConfig = mockBackendConfig;
        this.mockDataStore = mockDataStore;
    }

    private static final Logger logger = Logger.getLogger(MockChatRepository.class.getName());

    @Override
    public ChatModelDTO createChat(String uidFirebase, CreateChatDTO createChatDTO) throws InterruptedException {

        // Текущая дата и время для примера
        LocalDateTime now = LocalDateTime.now();

        // Имитация ожидания запроса
        Thread.sleep(mockBackendConfig.getDelay());

        String chatName = "Чат номер " + mockDataStore.chatCounter;
        if (!(createChatDTO.getMessage().isEmpty() || createChatDTO.getMessage().isBlank())){
            if (createChatDTO.getMessage().length()>21) {
                chatName = createChatDTO.getMessage().substring(0, 20) + "..";
            } else {
                chatName = createChatDTO.getMessage();
            }
        }

        mockDataStore.chats.add(new ChatModelDTO(
                (long)mockDataStore.chatCounter,
                mockBackendConfig.getUserId(),
                null,
                chatName,
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

        mockDataStore.chatCounter++;
        return mockDataStore.chats.get(mockDataStore.chats.size()-1);
    }

    @Override
    public ChatModelDTO createChatWithChatBot(String uidFirebase, CreateChatWithChatBotDTO createChatWithChatBotDTO) throws InterruptedException {


        // Текущая дата и время для примера
        LocalDateTime now = LocalDateTime.now();

        // Имитация ожидания запроса
        Thread.sleep(mockBackendConfig.getDelay());

        mockDataStore.chats.add(new ChatModelDTO(
                (long)mockDataStore.chatCounter,
                mockBackendConfig.getUserId(),
                createChatWithChatBotDTO.getChatBotId(),
                "Чат номер " + (long)mockDataStore.chatCounter,
                createChatWithChatBotDTO.getModelUriId(),
                createChatWithChatBotDTO.getTemperature(),
                createChatWithChatBotDTO.getContext(),
                false,
                mockChatBotRepository.mockResponseChatBot.get(Math.toIntExact(createChatWithChatBotDTO.getChatBotId())).isCanUseMemory(),
                mockChatBotRepository.mockResponseChatBot.get(Math.toIntExact(createChatWithChatBotDTO.getChatBotId())).isCanUpdateMemory(),
                mockChatBotRepository.mockResponseChatBot.get(Math.toIntExact(createChatWithChatBotDTO.getChatBotId())).isCanUseMemory(),
                mockChatBotRepository.mockResponseChatBot.get(Math.toIntExact(createChatWithChatBotDTO.getChatBotId())).isCanUpdateMemory(),
                mockChatBotRepository.mockResponseChatBot.get(Math.toIntExact(createChatWithChatBotDTO.getChatBotId())).isCanEditModelUri(),
                mockChatBotRepository.mockResponseChatBot.get(Math.toIntExact(createChatWithChatBotDTO.getChatBotId())).isCanEditContext(),
                now,
                now
        ));

        mockDataStore.chatCounter++;
        return mockDataStore.chats.get(mockDataStore.chats.size()-1);
    }

    @Override
    public List<ChatModelDTO> newGetUserChats(String uidFirebase) throws InterruptedException {

        // Имитация ожидания запроса
        Thread.sleep(mockBackendConfig.getDelay());

        return mockDataStore.chats.stream().filter(chat -> Objects.equals(chat.getUserId(), mockBackendConfig.getUserId())).toList();
    }

    @Override
    public ChatModelDTO getChatByChatId(String uidFirebase, Long chatId) throws InterruptedException {
        Thread.sleep(mockBackendConfig.getDelay());

        ChatModelDTO chat = findChatOrThrow(chatId);
        if (!Objects.equals(chat.getUserId(), mockBackendConfig.getUserId())) {
            throw new GlobalException("NOT_ALLOWED_CHAT", "Чат не доступен пользователю");
        }
        return chat;
    }

    @Override
    public ChatModelDTO newUpdateChatSettings(String uidFirebase,
                                              UpdateChatSettingsDTO dto) throws InterruptedException {
        Thread.sleep(mockBackendConfig.getDelay());

        ChatModelDTO chat = findChatOrThrow(dto.getChatId());

        chat.setModelUriId(dto.getModelUriId());
        chat.setTemperature(dto.getTemperature());
        chat.setContext(dto.getContext());
        chat.setUseMemory(dto.isUseMemory());
        chat.setUpdateMemory(dto.isUpdateMemory());
        chat.setStarredChat(dto.isStarredChat());

        return chat;
    }

    @Override
    public SuccessMessageDTO deleteChatByChatId(String uidFirebase, long chatId) throws InterruptedException {
        Thread.sleep(mockBackendConfig.getDelay());


        logger.info("Starting deleting messages");
        mockDataStore.messages.removeIf(
                m -> Objects.equals(m.getChatId(), chatId)
        );

        logger.info("Starting deleting chat");
        mockDataStore.chats.removeIf(c -> c.getId() == chatId);

        return new SuccessMessageDTO(true, "Чат был успешно удален");
    }

    private ChatModelDTO findChatOrThrow(long chatId) {
        return mockDataStore.chats.stream()
                .filter(c -> Objects.equals(c.getId(), chatId))
                .findFirst()
                .orElseThrow(() ->
                        new GlobalException("NOT_EXISTING_CHAT", "Чат с данным ID не существует"));
    }
}
