package com.promts.promts_test_server.repository.Message;

import com.promts.promts_test_server.dto.Message.inbound.MessageModelDTO;
import com.promts.promts_test_server.dto.Message.outbound.SaveMessageDTO;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Profile("model")
public class ModelMessageRepository implements MessageRepository{
    @Override
    public List<MessageModelDTO> getAllChatMessagesByChatId(String uidFirebase, Long id, Long chatId) {
        return List.of();
    }

    @Override
    public MessageModelDTO createNewMessage(String uidFirebase, Long id, Long chatId, SaveMessageDTO newMessage) {
        return null;
    }
}
