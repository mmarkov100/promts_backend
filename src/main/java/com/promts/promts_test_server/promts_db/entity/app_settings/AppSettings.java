package com.promts.promts_test_server.promts_db.entity.app_settings;

import com.promts.promts_test_server.promts_db.entity.modeluri.ModelUri;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Глобальные настройки приложения.  
 * В таблице должна храниться ровно одна запись (id=1),
 * поэтому доступ к ней лучше инкапсулировать в сервисе (AppSettingsService)
 * и отдавать наружу только методы get()/update().
 */
@Entity
@Table(name = "app_settings")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AppSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* --- Модели генерации --------------------------------------------------- */

    /** Какая модель назначается каждому новому пользователю. */
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "standard_model_uri_id",
                foreignKey = @ForeignKey(name = "fk_appsettings_std_model"))
    private ModelUri standardModelUri;

    /** Модель, которой обновляется «память» пользователя. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "standard_model_uri_memory_id",
                foreignKey = @ForeignKey(name = "fk_appsettings_std_memory_model"))
    private ModelUri standardModelUriMemory;

    /* --- финансы ----------------------------------------------------------- */

    /** Множитель комиссии для обычных сообщений. */
    @Column(name = "msg_cost_commission_ampl", nullable = false)
    private double messageCostCommissionAmpl;

    /** Множитель комиссии для системных сообщений. */
    @Column(name = "sys_msg_cost_commission_ampl", nullable = false)
    private double systemMessageCostCommissionAmpl;

    /** Стартовый баланс новых пользователей (₽, 10,4). */
    @Column(name = "start_money_rub", precision = 10, scale = 4, nullable = false)
    private BigDecimal startMoneyRub;

    /* --- глобальные правила генерации -------------------------------------- */

    @Column(name = "app_generation_rules", columnDefinition = "TEXT")
    private String appGenerationRules;

    @Builder.Default
    private boolean isUseGenerationRules = false;

    /* --- системные поля ----------------------------------------------------- */

    @CreationTimestamp
    @Column(name = "date_create", nullable = false, updatable = false)
    private LocalDateTime dateCreate;
}
