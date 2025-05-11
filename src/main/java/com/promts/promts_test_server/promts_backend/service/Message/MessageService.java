package com.promts.promts_test_server.promts_backend.service.Message;

import com.promts.promts_test_server.promts_backend.dto.Message.inbound.DeleteMessagesDTO;
import com.promts.promts_test_server.promts_backend.dto.Message.inbound.MessageModelDTO;
import com.promts.promts_test_server.promts_backend.dto.Message.inbound.NewMessageDTO;
import com.promts.promts_test_server.promts_backend.dto.Message.inbound.RegenerateMessageDTO;
import com.promts.promts_test_server.promts_backend.dto.Message.outbound.MessageGenerateResponse;
import com.promts.promts_test_server.promts_backend.dto.Message.outbound.SuccessDeleteMessagesDTO;

import java.util.List;

public interface MessageService {
    MessageGenerateResponse generateMessage(String uidFirebase, NewMessageDTO newMessageDTO) throws InterruptedException;
    MessageGenerateResponse regenerateMessage(String uidFirebase, RegenerateMessageDTO regenerateMessageDTO);
    List<MessageModelDTO> getMessagesByChatId(String uidFirebase, Long chatId) throws InterruptedException;
    SuccessDeleteMessagesDTO deleteMessagesByMessageId(String uidFirebase, DeleteMessagesDTO deleteMessagesDTO);
}
