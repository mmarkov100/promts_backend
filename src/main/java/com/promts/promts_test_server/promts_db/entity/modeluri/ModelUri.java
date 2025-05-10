package com.promts.promts_test_server.promts_db.entity.modeluri;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "model_uri")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ModelUri {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Название, видимое пользователю */
    @Column(nullable = false, length = 255)
    private String name;

    /** Уникальное системное имя (openrouter/deepseek‑v3 и т.д.) */
    @Column(name = "system_name", nullable = false, length = 255, unique = true)
    private String systemName;

    @Column(columnDefinition = "TEXT")
    private String description;

    private BigDecimal costRequestRub;
    private BigDecimal costInputTokensRub;
    private BigDecimal costOutputTokensRub;

    private Integer maxOutputTokens;
    private Integer maxContext;
    private BigDecimal minRubles;

    @Column(columnDefinition = "TEXT")
    private String ruleGeneration;

    @Builder.Default
    private boolean isUsingRules = false;

    @UpdateTimestamp
    private LocalDateTime dateEdit;

    @CreationTimestamp
    private LocalDateTime dateCreate;
}
