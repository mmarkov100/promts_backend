package com.promts.promts_test_server.promts_backend.repository.Message;

import com.promts.promts_test_server.promts_backend.config.MockBackendConfig;
import com.promts.promts_test_server.promts_backend.dto.Chat.inbound.ChatModelDTO;
import com.promts.promts_test_server.promts_backend.dto.Message.inbound.MessageModelDTO;
import com.promts.promts_test_server.promts_backend.dto.Message.outbound.SaveMessageDTO;
import com.promts.promts_test_server.shared.exception.GlobalException;
import com.promts.promts_test_server.promts_backend.repository.DataSore.MockDataStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@Profile({"mock", "neuro", "dbmock"})
public class MockMessageRepository implements MessageRepository{

    private final MockBackendConfig mockBackendConfig;
    private final MockDataStore mockDataStore;
    @Autowired
    public MockMessageRepository(MockBackendConfig mockBackendConfig, MockDataStore mockDataStore){
        this.mockBackendConfig = mockBackendConfig;
        this.mockDataStore = mockDataStore;
    }

    @Override
    public List<MessageModelDTO> getAllChatMessagesByChatId(String uidFirebase, Long chatId) {

        Optional<ChatModelDTO> chatModelOpt = mockDataStore.chats.stream()
                .filter(chat -> Objects.equals(chat.getId(), chatId))
                .findFirst();

        if (chatModelOpt.isEmpty()) {
            throw new GlobalException("NO_CHAT","Такого чата не существует или у текущего пользователя нет доступа к нему");
        }

        return mockDataStore.messages.stream()
                .filter(message -> !message.isOldMessage()
                        && !Objects.equals(message.getType(), "SYSTEM")
                        && Objects.equals(chatModelOpt.get().getUserId(), mockBackendConfig.getUserId())
                        && Objects.equals(message.getChatId(), chatId))
                .toList();
    }

    @Override
    public MessageModelDTO createNewMessage(String uidFirebase, Long chatId, SaveMessageDTO newMessage) {

        mockDataStore.messages.add(new MessageModelDTO(
                (long)mockDataStore.messageCounter,
                newMessage.getChatId(),
                newMessage.getModelUriId(),
                newMessage.isOldMessage(),
                newMessage.getRole(),
                newMessage.getText(),
                newMessage.getType(),
                LocalDateTime.now()
        ));
        mockDataStore.messageCounter++;
        return mockDataStore.messages.get(mockDataStore.messages.size()-1);
    }
}
