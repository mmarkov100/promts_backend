package com.promts.promts_test_server.promts_backend.repository.Chat;

import com.promts.promts_test_server.promts_backend.dto.Chat.inbound.ChatModelDTO;
import com.promts.promts_test_server.promts_backend.dto.Chat.inbound.CreateChatDTO;
import com.promts.promts_test_server.promts_backend.dto.Chat.inbound.CreateChatWithChatBotDTO;
import com.promts.promts_test_server.promts_backend.dto.Chat.inbound.UpdateChatSettingsDTO;
import com.promts.promts_test_server.promts_backend.dto.Shared.outbound.SuccessMessageDTO;
import com.promts.promts_test_server.promts_db.dto.chat.outbound.ChatDto; // Понадобится для ChatServiceDB
import com.promts.promts_test_server.promts_db.entity.chat.Chat;
import com.promts.promts_test_server.promts_db.service.chat.ChatServiceDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@Profile({"dbmock", "firebase"}) // Активируем только для профиля dbmock
public class DBMockProfileChatRepository implements ChatRepository {

    private final ChatServiceDB chatServiceDB;

    @Autowired
    public DBMockProfileChatRepository(ChatServiceDB chatServiceDB) {
        this.chatServiceDB = chatServiceDB;
    }

    @Override
    public ChatModelDTO createChat(String uidFirebase, CreateChatDTO createChatDTO) {
        ChatDto dbChatDto = ChatDto.builder()
                .modelUriId(createChatDTO.getModelUriId())
                .chatName(determineChatName(createChatDTO.getMessage()))
                .temperature(createChatDTO.getTemperature())
                .context(createChatDTO.getContext())
                .useMemory(createChatDTO.isUseMemory())
                .updateMemory(createChatDTO.isUpdateMemory())
                .starredChat(false) // По умолчанию
                .build();
        Chat createdChat = chatServiceDB.create(uidFirebase, dbChatDto);
        return convertToChatModelDTO(createdChat);
    }

    @Override
    public ChatModelDTO createChatWithChatBot(String uidFirebase, CreateChatWithChatBotDTO createChatWithChatBotDTO) {
        // Здесь нужно будет получить данные ChatBot, чтобы правильно установить поля can* и use*/update*
        // Предположим, у вас есть сервис для ChatBot, который может предоставить эти данные
        // ChatBotModelDTO chatBot = chatBotService.getChatBot(createChatWithChatBotDTO.getChatBotId(), uidFirebase);

        ChatDto dbChatDto = ChatDto.builder()
                .chatBotId(createChatWithChatBotDTO.getChatBotId())
                .modelUriId(createChatWithChatBotDTO.getModelUriId())
                .chatName("Чат с ботом " + createChatWithChatBotDTO.getChatBotId()) // Или более осмысленное имя
                .temperature(createChatWithChatBotDTO.getTemperature())
                .context(createChatWithChatBotDTO.getContext())
                // .useMemory(chatBot.isCanUseMemory() && createChatWithChatBotDTO.isUseMemory()) // Пример
                // .updateMemory(chatBot.isCanUpdateMemory() && createChatWithChatBotDTO.isUpdateMemory()) // Пример
                .starredChat(false)
                .build();
        Chat createdChat = chatServiceDB.create(uidFirebase, dbChatDto);
        return convertToChatModelDTO(createdChat);
    }

    @Override
    public List<ChatModelDTO> newGetUserChats(String uidFirebase) {
        List<Chat> dbChats = chatServiceDB.getChats(uidFirebase);
        return dbChats.stream().map(this::convertToChatModelDTO).collect(Collectors.toList());
    }

    //TODO Сделать позже
    @Override
    public ChatModelDTO getChatByChatId(String uidFirebase, Long chatId) {
        // ChatServiceDB не имеет прямого метода getChatByChatId для одного чата по uidFirebase и chatId,
        // но можно получить все чаты пользователя и отфильтровать, либо добавить такой метод в ChatServiceDB.
        // Пока что простой вариант с фильтрацией:
//        List<Chat> userChats = chatServiceDB.getChats(uidFirebase);
//        return userChats.stream()
//                .filter(chat -> chat.getId().equals(chatId))
//                .findFirst()
//                .map(this::convertToChatModelDTO)
//                .orElseThrow(() -> new RuntimeException("Chat not found or access denied")); // Используйте ваш GlobalException

        return null;
    }

    @Override
    public ChatModelDTO updateChatSettings(String uidFirebase, UpdateChatSettingsDTO updateChatSettingsDTO) {
        ChatDto dbChatDto = ChatDto.builder()
                .chatId(updateChatSettingsDTO.getChatId())
                .modelUriId(updateChatSettingsDTO.getModelUriId())
                .temperature(updateChatSettingsDTO.getTemperature())
                .context(updateChatSettingsDTO.getContext())
                .useMemory(updateChatSettingsDTO.isUseMemory())
                .updateMemory(updateChatSettingsDTO.isUpdateMemory())
                .starredChat(updateChatSettingsDTO.isStarredChat())
                .build();
        Chat updatedChat = chatServiceDB.update(uidFirebase, dbChatDto);
        return convertToChatModelDTO(updatedChat);
    }

    @Override
    public SuccessMessageDTO deleteChatByChatId(String uidFirebase, long chatId) {
        chatServiceDB.delete(uidFirebase, chatId);
        return new SuccessMessageDTO(true, "Чат был успешно удален из БД");
    }

    // Вспомогательный метод для конвертации из Chat (сущность БД) в ChatModelDTO (DTO бэкенда)
    private ChatModelDTO convertToChatModelDTO(Chat chat) {
        if (chat == null) return null;
        return new ChatModelDTO(
                chat.getId(),
                chat.getUser() != null ? chat.getUser().getId() : null,
                chat.getChatBot() != null ? chat.getChatBot().getId() : null,
                chat.getChatName(),
                chat.getModelUri() != null ? chat.getModelUri().getId() : null,
                chat.getTemperature(),
                chat.getContext(),
                chat.isStarredChat(),
                chat.isUseMemory(),
                chat.isUpdateMemory(),
                chat.isCanUseMemory(),
                chat.isCanUpdateMemory(),
                chat.isCanEditModelUri(),
                chat.isCanEditContext(),
                chat.getDateEdit(),
                chat.getDateCreate()
        );
    }

    // Вспомогательный метод для определения имени чата (из MockChatRepository)
    private String determineChatName(String message) {
        if (message == null || message.isBlank()) {
            // Можно генерировать уникальное имя или использовать стандартное
            return "Новый чат"; // Или например "Чат " + System.currentTimeMillis();
        }
        if (message.length() > 20) {
            return message.substring(0, 20) + "..";
        } else {
            return message;
        }
    }
}