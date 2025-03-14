package com.promts.promts_test_server.repository.Message;

import com.promts.promts_test_server.config.MockConfig;
import com.promts.promts_test_server.dto.Chat.inbound.ChatModelDTO;
import com.promts.promts_test_server.dto.Message.inbound.MessageModelDTO;
import com.promts.promts_test_server.dto.Message.outbound.SaveMessageDTO;
import com.promts.promts_test_server.exception.GlobalException;
import com.promts.promts_test_server.repository.Chat.MockChatRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@Profile("mock")
public class MockMessageRepository implements MessageRepository{

    @Autowired
    private MockChatRepository mockChatRepository;
    @Autowired
    private MockConfig mockConfig;

    @Override
    public List<MessageModelDTO> getAllChatMessagesByChatId(String uidFirebase, Long id, Long chatId) throws InterruptedException {

        // Имитация ожидания запроса
        Thread.sleep(mockConfig.getDelay());

        Optional<ChatModelDTO> chatModelOpt = mockChatRepository.mockResponseChats.stream()
                .filter(chat -> Objects.equals(chat.getId(), chatId))
                .findFirst();

        if (chatModelOpt.isEmpty()) {
            throw new GlobalException("NO_CHAT","Такого чата не существует или у текущего пользователя нет доступа к нему");
        }

        return mockMessageModel.stream()
                .filter(message -> !message.isOldMessage()
                        && !Objects.equals(message.getType(), "SYSTEM")
                        && Objects.equals(chatModelOpt.get().getUserId(), id)
                        && Objects.equals(message.getChatId(), chatId))
                .toList();
    }

    @Override
    public MessageModelDTO createNewMessage(String uidFirebase, Long id, Long chatId, SaveMessageDTO newMessage) throws InterruptedException {

        // Имитация ожидания запроса
        Thread.sleep(mockConfig.getDelay());

        mockMessageModel.add(new MessageModelDTO(
                (long) (mockMessageModel.size()+1),
                newMessage.getChatId(),
                newMessage.getModelUriId(),
                newMessage.isOldMessage(),
                newMessage.getRole(),
                newMessage.getText(),
                newMessage.getType(),
                LocalDateTime.now()
        ));
        return mockMessageModel.getLast();
    }

    // Контейнер для хранения сообщений
    public List<MessageModelDTO> mockMessageModel;

    @PostConstruct
    private void initMockMessageModel(){
        // Инициализируем список сообщений
        mockMessageModel = new ArrayList<>();
        long messageId = 1;

        // Чат 1: 10 сообщений, modelUriId = 2 (YandexGPT 5 Pro)
        for (int i = 0; i < 10; i++) {
            String role = (i % 2 == 0) ? "USER" : "ASSISTANT";
            Long modelId = (i % 2 == 0) ? null: 2L;
            MessageModelDTO message = new MessageModelDTO(
                    messageId++,
                    1L,                  // chatId = 1
                    modelId,                  // modelUriId = 2 (YandexGPT 5 Pro)
                    false,               // oldMessage
                    role,
                    "Сообщение " + (i+1) + " для чата 1",
                    "MESSAGE",           // type
                    LocalDateTime.now().minusMinutes(10 - i)  // пример времени создания
            );
            mockMessageModel.add(message);
        }

        // Чат 2: 10 сообщений, modelUriId = 3 (ChatGPT 4o mini)
        for (int i = 0; i < 10; i++) {
            String role = (i % 2 == 0) ? "USER" : "ASSISTANT";
            Long modelId = (i % 2 == 0) ? null: 3L;
            MessageModelDTO message = new MessageModelDTO(
                    messageId++,
                    2L,                  // chatId = 2
                    modelId,                  // modelUriId = 3 (ChatGPT 4o mini)
                    false,
                    role,
                    "Сообщение " + (i+1) + " для чата 2",
                    "MESSAGE",
                    LocalDateTime.now().minusMinutes(10 - i)
            );
            mockMessageModel.add(message);
        }

        // Чат 3: 2 сообщения, modelUriId = 1 (DeepSeek V3)
        for (int i = 0; i < 2; i++) {
            String role = (i % 2 == 0) ? "USER" : "ASSISTANT";
            Long modelId = (i % 2 == 0) ? null: 1L;
            MessageModelDTO message = new MessageModelDTO(
                    messageId++,
                    3L,                  // chatId = 3
                    modelId,                  // modelUriId = 1 (DeepSeek V3)
                    false,
                    role,
                    "Сообщение " + (i+1) + " для чата 3",
                    "MESSAGE",
                    LocalDateTime.now().minusMinutes(2 - i)
            );
            mockMessageModel.add(message);
        }

        // Чат 4: 2 сообщения, modelUriId = 1 (DeepSeek V3)
        for (int i = 0; i < 2; i++) {
            String role = (i % 2 == 0) ? "USER" : "ASSISTANT";
            Long modelId = (i % 2 == 0) ? null: 1L;
            MessageModelDTO message = new MessageModelDTO(
                    messageId++,
                    4L,                  // chatId = 4
                    modelId,                  // modelUriId = 1 (DeepSeek V3)
                    false,
                    role,
                    "Сообщение " + (i+1) + " для чата 4",
                    "MESSAGE",
                    LocalDateTime.now().minusMinutes(2 - i)
            );
            mockMessageModel.add(message);
        }
    }
}
