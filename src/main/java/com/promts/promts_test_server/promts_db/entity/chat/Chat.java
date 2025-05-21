package com.promts.promts_test_server.promts_db.entity.chat;

import com.promts.promts_test_server.promts_db.entity.chatbot.ChatBot;
import com.promts.promts_test_server.promts_db.entity.message.Message;
import com.promts.promts_test_server.promts_db.entity.modeluri.ModelUri;
import com.promts.promts_test_server.promts_db.entity.user.AppUser;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Обычный диалог пользователя.
 *
 * <ul>
 *   <li>У каждого чата всегда есть владелец({@link AppUser}).</li>
 *   <li>Чат может (но не обязан) быть привязан к{@link ChatBot} —при
 *       удалении бота ссылка зануляется.</li>
 *   <li>Пользователь может «открепить» чат от бота, но история останется.</li>
 * </ul>
 */
@Entity
@Table(name = "chat")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Chat {

    /* ------------ ключ и связи ---------------- */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Владелец чата. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id",
                nullable = false,
                foreignKey = @ForeignKey(name = "fk_chat_user"))
    private AppUser user;

    /** Чат‑бот, по шаблону которого создан чат (может быть <code>null</code>). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_bot_id",
                foreignKey = @ForeignKey(name = "fk_chat_chatbot"))
    private ChatBot chatBot;

    /** Модель генерации, назначенная для чата. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "model_uri_id",
                nullable = false,
                foreignKey = @ForeignKey(name = "fk_chat_model"))
    private ModelUri modelUri;

    /* ------------ данные чата ------------------ */

    @Column(name = "chat_name", nullable = false)
    private String chatName;

    private double temperature;

    /** Контекст разговора. */
    @Column(columnDefinition = "TEXT")
    private String context;

    private boolean starredChat;

    /* ------------ права и поведение ------------ */

    private boolean canUseMemory;
    private boolean canUpdateMemory;
    private boolean canEditModelUri;
    private boolean canEditContext;

    private boolean useMemory;
    private boolean updateMemory;

    /* ------------ системные поля --------------- */

    @UpdateTimestamp
    private LocalDateTime dateEdit;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreate;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages;
}
