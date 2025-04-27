package com.promts.promts_test_server.service.Message;

import com.promts.promts_test_server.dto.Message.inbound.DeleteMessagesDTO;
import com.promts.promts_test_server.dto.Message.inbound.MessageModelDTO;
import com.promts.promts_test_server.dto.Message.inbound.NewMessageDTO;
import com.promts.promts_test_server.dto.Message.inbound.RegenerateMessageDTO;
import com.promts.promts_test_server.dto.Message.outbound.MessageGenerateResponse;
import com.promts.promts_test_server.dto.Message.outbound.SuccessDeleteMessagesDTO;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Profile("model")
public class ModelMessageService implements MessageService{
    @Override
    public MessageGenerateResponse generateMessage(String uidFirebase, NewMessageDTO newMessageDTO) {
        return null;
    }

    @Override
    public MessageGenerateResponse regenerateMessage(String uidFirebase, RegenerateMessageDTO regenerateMessageDTO) {
        return null;
    }

    @Override
    public List<MessageModelDTO> newGetMessagesByChatId(String uidFirebase, Long chatId) throws InterruptedException {

        return List.of();
    }

    @Override
    public SuccessDeleteMessagesDTO deleteMessagesByMessageId(String uidFirebase, DeleteMessagesDTO deleteMessagesDTO) {
        return null;
    }
}
