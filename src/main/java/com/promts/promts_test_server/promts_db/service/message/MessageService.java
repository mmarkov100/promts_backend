package com.promts.promts_test_server.promts_db.service.message;

import com.promts.promts_test_server.promts_db.entity.message.Message;
import com.promts.promts_test_server.promts_db.repository.ChatRepository;
import com.promts.promts_test_server.promts_db.repository.MessageRepository;
import com.promts.promts_test_server.promts_db.repository.ModelUriRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository repo;
    private final ChatRepository chatRepo;
    private final ModelUriRepository modelRepo;

    /** Отправить новое сообщение в чат. */
    public Message send(Long chatId, String role, String text, Long modelUriId) {
        Message m = Message.builder()
                .chat(chatRepo.getReferenceById(chatId))
                .role(role)
                .text(text)
                .type("MESSAGE")
                .modelUri(modelUriId == null ? null : modelRepo.getReferenceById(modelUriId))
                .build();
        return repo.save(m);
    }

    /** Отметить сообщение как «старое» (используется при регенерации или удалении). */
    @Transactional
    public void markOld(Long messageId) {
        repo.findById(messageId).ifPresent(m -> {
            m.setOldMessage(true);
            repo.save(m);
        });
    }

    /** История чата без «старых» и системных сообщений. */
    public List<Message> history(Long chatId) {
        return repo.findByChatIdAndOldMessageFalseOrderByDateCreate(chatId);
    }
}
