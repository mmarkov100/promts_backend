package com.promts.promts_test_server.promts_db.entity.transaction;

import com.promts.promts_test_server.promts_db.entity.message.Message;
import com.promts.promts_test_server.promts_db.entity.user.AppUser;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment_transaction")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PaymentTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* --- связи ------------------------------------------------------------- */

    /** Пользователь, чей баланс изменился (NOT NULL). */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_pt_user"))
    private AppUser user;

    /** Администратор‑инициатор (NULL, если транзакцию спровоцировал сам пользователь). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator_user_id",
            foreignKey = @ForeignKey(name = "fk_pt_initiator_user"))
    private AppUser initiatorUser;

    /** Сообщение‑источник затрат на генерацию (NULL для пополнений/снятий). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id",
            foreignKey = @ForeignKey(name = "fk_pt_message"))
    private Message message;

    /* --- финансовые данные ------------------------------------------------- */

    /** Изменение баланса (– списание / + пополнение). */
    @Column(name = "change_money_rub", precision = 10, scale = 4, nullable = false)
    private BigDecimal changeMoneyRub;

    /** Сколько входных токенов было у запроса (NULL, если не генерация). */
    private Integer inputTokens;

    /** Сколько всего токенов было у запроса (NULL, если не генерация). */
    private Integer totalTokens;

    /** Стоимость генерации без комиссии (NULL, если не генерация). */
    @Column(precision = 10, scale = 4)
    private BigDecimal cost;

    /** Стоимость генерации с учётом комиссии (NULL, если не генерация). */
    @Column(name = "cost_commission", precision = 10, scale = 4)
    private BigDecimal costCommission;

    /** Тип операции. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private TransactionType type;

    /** Текстовое пояснение. */
    @Column(columnDefinition = "TEXT", name = "description")
    private String desc;

    /* --- системное поле ---------------------------------------------------- */

    @CreationTimestamp
    @Column(name = "date_create", nullable = false, updatable = false)
    private LocalDateTime dateCreate;

    /* ====================================================================== */

    public enum TransactionType {
        TOP_UP,              // Пополнение
        WITHDRAWAL,          // Снятие
        MESSAGE_GENERATION,  // Генерация сообщения
        SYSTEM,              // Системное списание/зачисление
        EMBEDDING            // Генерация эмбединга
    }
}
