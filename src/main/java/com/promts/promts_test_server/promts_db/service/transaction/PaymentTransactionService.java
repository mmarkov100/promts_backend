package com.promts.promts_test_server.promts_db.service.transaction;

import com.promts.promts_test_server.promts_db.entity.transaction.PaymentTransaction;
import com.promts.promts_test_server.promts_db.repository.AppUserRepository;
import com.promts.promts_test_server.promts_db.repository.MessageRepository;
import com.promts.promts_test_server.promts_db.repository.PaymentTransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PaymentTransactionService {

    private final PaymentTransactionRepository repo;
    private final AppUserRepository userRepo;
    private final MessageRepository messageRepo;

    /** Записать транзакцию, привязанную к сообщению‑генерации. */
    @Transactional
    public PaymentTransaction logGeneration(Long userId,
                                            Long messageId,
                                            BigDecimal cost,
                                            BigDecimal costWithCommission,
                                            int inputTokens,
                                            int totalTokens) {

        PaymentTransaction tx =PaymentTransaction.builder()
                .user(userRepo.getReferenceById(userId))
                .message(messageRepo.getReferenceById(messageId))
                .changeMoneyRub(costWithCommission.negate())
                .cost(cost)
                .costCommission(costWithCommission)
                .inputTokens(inputTokens)
                .totalTokens(totalTokens)
                .type(PaymentTransaction.TransactionType.MESSAGE_GENERATION)
                .desc("Message generation, messageId=" + messageId)
                .build();

        return repo.save(tx);
    }


    /** Пополнение/списание администратором. */
    @Transactional
    public PaymentTransaction manualChange(Long targetUserId,
                                           Long adminId,
                                           BigDecimal delta,
                                           String description) {

        PaymentTransaction tx = PaymentTransaction.builder()
                .user(userRepo.getReferenceById(targetUserId))
                .initiatorUser(userRepo.getReferenceById(adminId))
                .changeMoneyRub(delta)
                .type(delta.signum() >= 0 ?
                        PaymentTransaction.TransactionType.TOP_UP :
                        PaymentTransaction.TransactionType.WITHDRAWAL)
                .desc(description)
                .build();

        return repo.save(tx);
    }
}
