package com.promts.promts_test_server.promts_backend.repository.Message;

/*
Репозиторий для обращения в бд по сообщениям
 */

import com.promts.promts_test_server.promts_backend.dto.Message.inbound.MessageModelDTO;
import com.promts.promts_test_server.promts_backend.dto.Message.outbound.SaveMessageDTO;


import java.util.List;

public interface MessageRepository {
    List<MessageModelDTO> getAllChatMessagesByChatId (String uidFirebase, Long chatId) throws InterruptedException;
    MessageModelDTO createNewMessage(String uidFirebase, Long chatId, SaveMessageDTO newMessage) throws InterruptedException;
}
