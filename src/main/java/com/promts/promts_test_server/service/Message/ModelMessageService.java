package com.promts.promts_test_server.service.Message;

import com.promts.promts_test_server.dto.Message.inbound.DeleteMessagesDTO;
import com.promts.promts_test_server.dto.Message.inbound.NewMessageDTO;
import com.promts.promts_test_server.dto.Message.inbound.RegenerateMessageDTO;
import com.promts.promts_test_server.dto.Message.outbound.ListMessages;
import com.promts.promts_test_server.dto.Message.outbound.MessageGenerateResponse;
import com.promts.promts_test_server.dto.Message.outbound.SuccessDeleteMessagesDTO;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("model")
public class ModelMessageService implements MessageService{
    @Override
    public MessageGenerateResponse generateMessage(String uidFirebase, Long id, NewMessageDTO newMessageDTO) {
        return null;
    }

    @Override
    public MessageGenerateResponse regenerateMessage(String uidFirebase, Long id, RegenerateMessageDTO regenerateMessageDTO) {
        return null;
    }

    @Override
    public ListMessages getMessagesByChatId(String uidFirebase, Long id, Long chatId) {
        return null;
    }

    @Override
    public SuccessDeleteMessagesDTO deleteMessagesByMessageId(String uidFirebase, Long id, DeleteMessagesDTO deleteMessagesDTO) {
        return null;
    }
}
