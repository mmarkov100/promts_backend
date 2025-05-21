package com.promts.promts_test_server.promts_db.service.transaction;

import com.promts.promts_test_server.promts_db.entity.app_settings.AppSettings;
import com.promts.promts_test_server.promts_db.entity.transaction.PaymentTransaction;
import com.promts.promts_test_server.promts_db.entity.user.AppUser; // Импорт AppUser
import com.promts.promts_test_server.promts_db.repository.AppSettingsRepository;
import com.promts.promts_test_server.promts_db.repository.AppUserRepository;
import com.promts.promts_test_server.promts_db.repository.MessageRepository;
import com.promts.promts_test_server.promts_db.repository.PaymentTransactionRepository;
import com.promts.promts_test_server.shared.exception.GlobalException; // Импорт GlobalException
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode; // Для округления
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PaymentTransactionService {

    private final PaymentTransactionRepository repo;
    private final AppUserRepository userRepo;
    private final MessageRepository messageRepo;
    private final AppSettingsRepository appRepo; // Убедитесь, что это AppSettingsRepository

    /**
     * Записать транзакцию, привязанную к генерации (сообщения или памяти),
     * и обновить баланс пользователя.
     */
    @Transactional
    public PaymentTransaction logGenerationAndUpdateBalance(Long userId,
                                                            Long messageId,
                                                            BigDecimal costWithoutCommission, // Стоимость без комиссии
                                                            int inputTokens,
                                                            int totalTokens,
                                                            String messageTypeForCommission) { // Тип сообщения для определения комиссии
        AppUser user = userRepo.findById(userId)
                .orElseThrow(() -> new GlobalException("USER_NOT_FOUND", "Пользователь " + userId + " для транзакции не найден."));

        AppSettings appSettings = appRepo.findById(0L)
                .orElseThrow(() -> new GlobalException("APP_SETTINGS_NOT_FOUND", "Настройки приложения не найдены."));

        BigDecimal commissionMultiplier;
        String transactionDescription;
        String transactionType;

        if (Objects.equals(messageTypeForCommission, "SYSTEM_MEMORY")) {
            commissionMultiplier = BigDecimal.valueOf(appSettings.getSystemMessageCostCommissionAmpl());
            transactionDescription = "Memory update generation, messageId=" + messageId;
            // Можно оставить MESSAGE_GENERATION или выделить в SYSTEM, если это важно для аналитики
        } else { // MessageType.MESSAGE
            commissionMultiplier = BigDecimal.valueOf(appSettings.getMessageCostCommissionAmpl());
            transactionDescription = "Message generation, messageId=" + messageId;
        }
        transactionType = "MESSAGE_GENERATION";

        BigDecimal costWithCommission = costWithoutCommission.multiply(commissionMultiplier).setScale(4, RoundingMode.HALF_UP);
        BigDecimal changeMoneyRub = costWithCommission.negate(); // Списание, поэтому отрицательное

        // Проверка баланса
        if (user.getMoney().compareTo(costWithCommission) < 0) {
            throw new GlobalException("INSUFFICIENT_FUNDS", "Недостаточно средств для выполнения операции. Требуется: " + costWithCommission + ", доступно: " + user.getMoney());
        }

        // Списание средств
        user.setMoney(user.getMoney().subtract(costWithCommission));
        userRepo.save(user); // Сохраняем обновленного пользователя

        PaymentTransaction tx = PaymentTransaction.builder()
                .user(user) // Передаем загруженного пользователя
                .message(messageRepo.getReferenceById(messageId))
                .changeMoneyRub(changeMoneyRub) // Отрицательное значение для списания
                .cost(costWithoutCommission) // Стоимость без комиссии
                .costCommission(costWithCommission) // Стоимость с комиссией
                .inputTokens(inputTokens)
                .totalTokens(totalTokens)
                .type(transactionType)
                .desc(transactionDescription)
                // dateCreate установится автоматически
                .build();
        return repo.save(tx);
    }


    /** Пополнение/списание администратором. */
    @Transactional
    public PaymentTransaction manualChange(Long targetUserId,
                                           Long adminId, // Может быть null, если операция системная без админа
                                           BigDecimal delta, // Положительное для пополнения, отрицательное для списания
                                           String description,
                                           String type) { // Явно указываем тип

        AppUser targetUser = userRepo.findById(targetUserId)
                .orElseThrow(() -> new GlobalException("USER_NOT_FOUND", "Целевой пользователь " + targetUserId + " не найден."));

        AppUser initiator = null;
        if (adminId != null) {
            initiator = userRepo.findById(adminId)
                    .orElseThrow(() -> new GlobalException("USER_NOT_FOUND", "Администратор-инициатор " + adminId + " не найден."));
            if (initiator.getRole() != com.promts.promts_test_server.promts_db.entity.user.UserRole.ADMIN) {
                throw new GlobalException("ACCESS_DENIED", "Пользователь " + adminId + " не является администратором.");
            }
        }

        // Проверка баланса при списании
        if (delta.compareTo(BigDecimal.ZERO) < 0 && targetUser.getMoney().add(delta).compareTo(BigDecimal.ZERO) < 0) {
            // add(delta) т.к. delta уже отрицательная для списания
            throw new GlobalException("INSUFFICIENT_FUNDS", "Недостаточно средств для списания. Требуется списать: " + delta.abs() + ", доступно: " + targetUser.getMoney());
        }

        targetUser.setMoney(targetUser.getMoney().add(delta));
        userRepo.save(targetUser);

        PaymentTransaction tx = PaymentTransaction.builder()
                .user(targetUser)
                .initiatorUser(initiator) // Может быть null
                .changeMoneyRub(delta)
                .type(type) // Используем переданный тип
                .desc(description)
                .build();

        return repo.save(tx);
    }
}