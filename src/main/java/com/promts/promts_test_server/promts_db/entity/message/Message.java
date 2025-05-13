package com.promts.promts_test_server.promts_db.entity.message;

import com.promts.promts_test_server.promts_db.entity.chat.Chat;
import com.promts.promts_test_server.promts_db.entity.modeluri.ModelUri;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Array;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

/**
 * Сообщение в чате.
 * <p>
 * *Эмбединг* хранится в PostgreSQL‑расширении <b>pgvector</b>
 * и маппируется через модуль <b>hibernate‑vector</b>.
 */
@Entity
@Table(name = "message")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Message {

    /* -------- ключ и связи -------- */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Чат‑владелец сообщения. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "chat_id",
                nullable = false,
                foreignKey = @ForeignKey(name = "fk_message_chat"))
    private Chat chat;

    /** Нейросеть, сгенерировавшая сообщение (может быть <code>null</code> для сообщений пользователя). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_uri_id",
                foreignKey = @ForeignKey(name = "fk_message_model"))
    private ModelUri modelUri;

    /* -------- данные сообщения -------- */

    /**
     * Векторное представление текста.
     * <p>
     * Настройте размер под свою модель (пример: 1536).
     * Тип колонки — <code>vector</code> из pgvector.
     */
    @Array(length = 1536)
    @JdbcTypeCode(SqlTypes.VECTOR)
    @Column(columnDefinition = "vector")
    private float[] embedding;

    /** Помечается <b>true</b>, если сообщение «устарело» (например, заменено при регенерации). */
    @Builder.Default
    private boolean oldMessage = false;

    /** Роль источника текста: USER / ASSISTANT / SYSTEM … */
    @Column(nullable = false, length = 32)
    private String role;

    /** Сам текст (до 10 000 симв.). */
    @Column(columnDefinition = "TEXT", nullable = false)
    private String text;

    /** Тип сообщения: MESSAGE / SYSTEM_MEMORY / … */
    @Column(nullable = false, length = 32)
    private String type;

    /* -------- системные поля -------- */

    @CreationTimestamp
    @Column(name = "date_create", nullable = false, updatable = false)
    private LocalDateTime dateCreate;
}
