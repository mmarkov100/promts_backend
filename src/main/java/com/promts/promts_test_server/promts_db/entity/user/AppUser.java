package com.promts.promts_test_server.promts_db.entity.user;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "app_user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255, nullable = false, unique = true)
    private String email;

    @Column(name = "uid_firebase", nullable = false, unique = true, length = 128)
    private String uidFirebase;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    @Builder.Default
    private UserRole role = UserRole.USER;

    @Column(name = "standard_model_uri_id")
    private Long standardModelUriId;

    @Column(nullable = false, precision = 10, scale = 4)
    @Builder.Default
    private BigDecimal money = BigDecimal.valueOf((20.00));

    /**
     * Конфиденциальная «память» пользователя. При необходимости
     * замените на шифрование через {@link jakarta.persistence.AttributeConverter}.
     */
    @Column(columnDefinition = "TEXT")
    private String memory;

    @Builder.Default
    private boolean memoryEnabled = true;

    @Builder.Default
    private boolean aiCanUpdateMemory = true;

    @CreationTimestamp
    @Column(name = "date_create", nullable = false, updatable = false)
    private LocalDateTime dateCreate = LocalDateTime.now();
}
