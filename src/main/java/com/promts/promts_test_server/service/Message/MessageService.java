package com.promts.promts_test_server.service.Message;

import com.promts.promts_test_server.dto.Message.inbound.DeleteMessagesDTO;
import com.promts.promts_test_server.dto.Message.inbound.NewMessageDTO;
import com.promts.promts_test_server.dto.Message.inbound.RegenerateMessageDTO;
import com.promts.promts_test_server.dto.Message.outbound.ListMessages;
import com.promts.promts_test_server.dto.Message.outbound.MessageGenerateResponse;
import com.promts.promts_test_server.dto.Message.outbound.SuccessDeleteMessagesDTO;

public interface MessageService {
    MessageGenerateResponse generateMessage(String uidFirebase, Long id, NewMessageDTO newMessageDTO) throws InterruptedException;
    MessageGenerateResponse regenerateMessage(String uidFirebase, Long id, RegenerateMessageDTO regenerateMessageDTO);
    ListMessages getMessagesByChatId(String uidFirebase, Long id, Long chatId) throws InterruptedException;
    SuccessDeleteMessagesDTO deleteMessagesByMessageId(String uidFirebase, Long id, DeleteMessagesDTO deleteMessagesDTO);
}
