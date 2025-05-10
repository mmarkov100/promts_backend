package com.promts.promts_test_server.promts_backend.repository.DataSore;

import com.promts.promts_test_server.promts_backend.dto.Chat.inbound.ChatModelDTO;
import com.promts.promts_test_server.promts_backend.dto.Message.inbound.MessageModelDTO;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@Profile({"mock","neuro"})
public class MockDataStore {
    public Integer chatCounter = 0;
    public final List<ChatModelDTO> chats = new CopyOnWriteArrayList<>();
    public Integer messageCounter = 0;
    public final List<MessageModelDTO> messages = new CopyOnWriteArrayList<>();

    @PostConstruct
    void init() {
        initChats();
        initMessages();
    }

    private void initMessages(){
        // Чат 0: 10 сообщений, modelUriId = 2 (YandexGPT 5 Pro)
        for (int i = 0; i < 10; i++) {
            String role = (i % 2 == 0) ? "user" : "assistant";
            Long modelId = (i % 2 == 0) ? null: 2L;
            MessageModelDTO message = new MessageModelDTO(
                    (long)messageCounter,
                    0L,
                    modelId,
                    false,
                    role,
                    "Сообщение " + (i+1) + " для чата 1",
                    "MESSAGE",
                    LocalDateTime.now().minusMinutes(10 - i)
            );
            messages.add(message);
            messageCounter++;
        }

        // Чат 1: 2 сообщения, modelUriId = 1 (DeepSeek V3)
        for (int i = 0; i < 2; i++) {
            String role = (i % 2 == 0) ? "user" : "assistant";
            Long modelId = (i % 2 == 0) ? null: 1L;
            MessageModelDTO message = new MessageModelDTO(
                    (long)messageCounter,
                    1L,
                    modelId,
                    false,
                    role,
                    "Сообщение " + (i+1) + " для чата 3",
                    "MESSAGE",
                    LocalDateTime.now().minusMinutes(2 - i)
            );
            messages.add(message);
            messageCounter++;
        }

        // Чат 2: 2 сообщения, modelUriId = 1 (DeepSeek V3)
        for (int i = 0; i < 2; i++) {
            String role = (i % 2 == 0) ? "user" : "assistant";
            Long modelId = (i % 2 == 0) ? null: 1L;
            MessageModelDTO message = new MessageModelDTO(
                    (long)messageCounter,
                    2L,
                    modelId,
                    false,
                    role,
                    "Сообщение " + (i+1) + " для чата 4",
                    "MESSAGE",
                    LocalDateTime.now().minusMinutes(2 - i)
            );
            messages.add(message);
            messageCounter++;
        }
    }

    private void initChats(){
        // Текущая дата и время для примера
        LocalDateTime now = LocalDateTime.now();

        chats.add(new ChatModelDTO(
                (long)chatCounter,
                1L,
                null,
                "Обычный чат 1",
                0L,
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
        chatCounter++;

        chats.add(new ChatModelDTO(
                (long)chatCounter,
                1L,
                1L,
                "Чат с ботом",
                1L,
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
        chatCounter++;

        chats.add(new ChatModelDTO(
                (long)chatCounter,
                1L,
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
        chatCounter++;

        chats.add(new ChatModelDTO(
                (long)chatCounter,
                1L,
                null,
                "Закрепленный чат",
                0L,
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
        chatCounter++;
    }
}