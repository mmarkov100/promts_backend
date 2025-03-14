package com.promts.promts_test_server.repository.Message;

/*
Репозиторий для обращения в бд по сообщениям
 */

import com.promts.promts_test_server.dto.Message.inbound.MessageModelDTO;
import com.promts.promts_test_server.dto.Message.outbound.SaveMessageDTO;


import java.util.List;

public interface MessageRepository {
    List<MessageModelDTO> getAllChatMessagesByChatId (String uidFirebase, Long id, Long chatId) throws InterruptedException;
    MessageModelDTO createNewMessage(String uidFirebase, Long id, Long chatId, SaveMessageDTO newMessage) throws InterruptedException;
}
