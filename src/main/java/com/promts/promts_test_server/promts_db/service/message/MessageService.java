package com.promts.promts_test_server.promts_db.service.message;

import com.promts.promts_test_server.promts_db.dto.message.outbound.MessageDbDto;
import com.promts.promts_test_server.promts_db.entity.chat.Chat;
import com.promts.promts_test_server.promts_db.entity.message.Message;
import com.promts.promts_test_server.promts_db.entity.user.AppUser;
import com.promts.promts_test_server.promts_db.repository.*;
import com.promts.promts_test_server.promts_db.service.transaction.PaymentTransactionService;
import com.promts.promts_test_server.shared.exception.GlobalException;
import jakarta.transaction.Transactional; // Убедитесь, что импорт Transactional из jakarta.transaction
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class MessageService {

    private static final Logger logger = Logger.getLogger(MessageService.class.getName());

    private final MessageRepository repo;
    private final AppUserRepository userRepo;
    private final ChatRepository chatRepo;
    private final ModelUriRepository modelRepo;
    private final PaymentTransactionService paymentService;

    @Transactional
    public Message send(String uidFirebase,
                        MessageDbDto dto,
                        BigDecimal cost,
                        int inputTokens,
                        int totalTokens) {
        AppUser user = userRepo.findByUidFirebase(uidFirebase)
                .orElseThrow(() -> new GlobalException("USER_NOT_FOUND", "Пользователь с uidFirebase " + uidFirebase + " не найден."));

        Chat chat = chatRepo.findById(dto.getChatId())
                .orElseThrow(() -> new GlobalException("CHAT_NOT_FOUND", "Чат с ID " + dto.getChatId() + " не найден."));

        // Проверка, что пользователь является владельцем чата
        if (!Objects.equals(chat.getUser().getId(), user.getId())) {
            throw new GlobalException("ACCESS_DENIED", "Пользователь не является владельцем чата " + dto.getChatId());
        }

        String messageType;
        try {
            messageType = dto.getType().toUpperCase();
        } catch (IllegalArgumentException e) {
            throw new GlobalException("INVALID_MESSAGE_TYPE", "Недопустимый тип сообщения: " + dto.getType());
        }

        Message newMessage = Message.builder()
                .chat(chat)
                .role(dto.getRole()) // USER, ASSISTANT
                .text(dto.getText())
                .type(messageType) // MESSAGE, SYSTEM_MEMORY
                .modelUri(dto.getModelUriId() == null ? null : modelRepo.getReferenceById(dto.getModelUriId()))
                .oldMessage(dto.isOldMessage()) // Обычно false для новых сообщений
                .build();

        Message savedMessage = repo.save(newMessage);

        // Логируем транзакцию и списываем средства, ТОЛЬКО если это сообщение от ASSISTANT или SYSTEM_MEMORY
        // и есть стоимость (cost > 0). Сообщения пользователя не должны иметь стоимость генерации.
        if ((savedMessage.getRole().equalsIgnoreCase("ASSISTANT") || Objects.equals(savedMessage.getType(), "SYSTEM_MEMORY"))
                && cost != null && cost.compareTo(BigDecimal.ZERO) > 0) {

            // Передаем ID пользователя, а не весь объект user, т.к. PaymentTransactionService сам его загрузит
            paymentService.logGenerationAndUpdateBalance(
                    user.getId(), // ID пользователя, с которого списываем
                    savedMessage.getId(),
                    cost, // Стоимость без комиссии
                    inputTokens,
                    totalTokens,
                    savedMessage.getType() // Передаем тип сообщения для определения комиссии
            );
        }
        return savedMessage;
    }

    // Метод для сохранения "сырых" сообщений без логирования транзакций (например, чисто пользовательские)
    // Или если транзакция логируется отдельно более высокоуровневым сервисом
    @Transactional
    public void savePureMessage(MessageDbDto dto) {
        Chat chat = chatRepo.findById(dto.getChatId())
                .orElseThrow(() -> new GlobalException("CHAT_NOT_FOUND", "Чат с ID " + dto.getChatId() + " не найден."));

        String messageType;
        try {
            messageType = dto.getType().toUpperCase();
        } catch (IllegalArgumentException e) {
            throw new GlobalException("INVALID_MESSAGE_TYPE", "Недопустимый тип сообщения: " + dto.getType());
        }

        Message newMessage = Message.builder()
                .chat(chat)
                .role(dto.getRole())
                .text(dto.getText())
                .type(messageType)
                .modelUri(dto.getModelUriId() == null ? null : modelRepo.getReferenceById(dto.getModelUriId()))
                .oldMessage(dto.isOldMessage())
                .build();
        repo.save(newMessage);
    }


    /**
     * История чата без «старых» и (опционально) системных сообщений.
     */
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<Message> history(String uidFirebase, Long chatId, boolean includeSystemMemoryMessages) {
        logger.info("Запрос на получение сообщений по chatId: " + chatId);
        AppUser user = userRepo.findByUidFirebase(uidFirebase)
                .orElseThrow(() -> new GlobalException("USER_NOT_FOUND", "Пользователь " + uidFirebase + " не найден."));
        Chat chat = chatRepo.findById(chatId)
                .orElseThrow(() -> new GlobalException("CHAT_NOT_FOUND", "Чат " + chatId + " не найден."));

        if (!Objects.equals(user.getId(), chat.getUser().getId())) {
            throw new GlobalException("ACCESS_DENIED", "Доступ к чату " + chatId + " запрещен.");
        }

        if (includeSystemMemoryMessages) {
            // Эта логика должна быть в репозитории или здесь через фильтрацию
            // Пока оставим простой вызов, но его нужно будет доработать для MessageType
            return repo.findByChatIdAndOldMessageFalseOrderByDateCreate(chatId); // Примерный метод, нужно создать в репозитории
        } else {
            List<Message> messages = repo.findByChatIdAndOldMessageFalseAndTypeOrderByDateCreate(chatId, "MESSAGE"); // Только тип MESSAGE;
            logger.info(messages.toString());
            return messages; // Примерный метод, нужно создать в репозитории
        }
    }
}