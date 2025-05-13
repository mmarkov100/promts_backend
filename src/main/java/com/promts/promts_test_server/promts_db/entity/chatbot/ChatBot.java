package com.promts.promts_test_server.promts_db.entity.chatbot;

import com.promts.promts_test_server.promts_db.entity.modeluri.ModelUri;
import com.promts.promts_test_server.promts_db.entity.user.AppUser;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Чат‑бот, который может создавать любой пользователь.
 * <p>
 * Некоторые поля (canUseMemory… canEditContext) жёстко фиксируются при
 * создании и потом переносятся в создаваемые от бота чаты.
 */
@Entity
@Table(name = "chat_bot")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ChatBot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* --- связи ---------------------------------------------------------------- */

    /** Владелец чат‑бота. Может быть <code>null</code>, если бот опубликован админами. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",
                foreignKey = @ForeignKey(name = "fk_chatbot_user"))
    private AppUser user;

    /** Модель генерации, привязанная к боту (обязательна). */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "model_uri_id",
                nullable = false,
                foreignKey = @ForeignKey(name = "fk_chatbot_model"))
    private ModelUri modelUri;

    /* --- основные поля -------------------------------------------------------- */

    @Column(name = "chat_bot_name", nullable = false)
    private String chatBotName;

    @Column(name = "chat_bot_desc", columnDefinition = "TEXT")
    private String chatBotDesc;

    @Column(name = "hello_message", columnDefinition = "TEXT")
    private String helloMessage;

    private double temperature;

    /** До 10 000 симв. — ограничение на уровне DTO и фронта. */
    @Column(columnDefinition = "TEXT")
    private String context;

    @Builder.Default
    private boolean isSelectedByRedact = false;

    /* --- политика памяти и настроек ------------------------------------------ */

    @Builder.Default
    private boolean canUseMemory = true;

    @Builder.Default
    private boolean canUpdateMemory = true;

    @Builder.Default
    private boolean canEditModelUri = true;

    @Builder.Default
    private boolean canEditContext = true;

    /* --- системные поля ------------------------------------------------------- */

    @UpdateTimestamp
    private LocalDateTime dateEdit;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreate;
}
