package com.promts.promts_test_server.promts_db.service.user;

import com.promts.promts_test_server.promts_db.dto.user.outbound.AppUserDto;
import com.promts.promts_test_server.promts_db.entity.app_settings.AppSettings;
import com.promts.promts_test_server.promts_db.entity.user.AppUser;
import com.promts.promts_test_server.promts_db.repository.AppSettingsRepository;
import com.promts.promts_test_server.promts_db.repository.AppUserRepository;
import com.promts.promts_test_server.shared.exception.GlobalException; // Убедитесь в наличии этого импорта
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional; // Используйте jakarta.transaction.Transactional

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class AppUserService {

    private static final Logger logger = Logger.getLogger(AppUserService.class.getName());

    private final AppUserRepository repoUser;
    private final AppSettingsRepository repoSettings; // Предполагается, что это AppSettingsRepository

    @Transactional
    public AppUserDto updateUserSettings(AppUserDto dto) {
        AppUser user = repoUser.findByUidFirebase(dto.getUidFirebase())
                .orElseThrow(() -> new GlobalException("USER_NOT_FOUND", "Пользователь с uidFirebase " + dto.getUidFirebase() + " не найден."));

        // Обновляем только те поля, которые передаются в DTO для настроек
        if (dto.getMemory() != null) {
            user.setMemory(dto.getMemory());
        }
        // memoryEnabled и aiCanUpdateMemory являются boolean, поэтому они всегда будут иметь значение в DTO
        // (по умолчанию false, если не переданы явно, но Lombok @Builder.Default может это изменить)
        // В UpdateUserRequestDTO они не помечены как @Nullable, так что всегда будут.
        user.setMemoryEnabled(dto.isMemoryEnabled());
        user.setAiCanUpdateMemory(dto.isAiCanUpdateMemory());

        if (dto.getStandardModelUriId() != null) {
            user.setStandardModelUriId(dto.getStandardModelUriId());
        }
        // Поле money НЕ ДОЛЖНО обновляться через этот метод напрямую.
        // Баланс изменяется через PaymentTransactionService.

        return toDto(repoUser.save(user));
    }

    @Transactional // Для атомарности создания
    public AppUserDto createUser(AppUserDto dto) {

        logger.info("Creating user");

        if (repoUser.findByUidFirebase(dto.getUidFirebase()).isPresent()) {
            throw new GlobalException("USER_ALREADY_EXISTS", "Пользователь с uidFirebase " + dto.getUidFirebase() + " уже существует.");
        }
        if (repoUser.findByEmail(dto.getEmail()).isPresent()) { // Добавим проверку по email, если он тоже должен быть уникальным
            throw new GlobalException("EMAIL_ALREADY_EXISTS", "Пользователь с email " + dto.getEmail() + " уже существует.");
        }

        logger.info("User is not existed");

        AppUser user = fromDto(dto); // Конвертируем DTO в сущность

        AppSettings settings = repoSettings.findById(0L)
                .orElseThrow(() -> new GlobalException("APP_SETTINGS_NOT_FOUND", "Настройки приложения не найдены."));

        logger.info("Creating user");

        user.setRole(com.promts.promts_test_server.promts_db.entity.user.UserRole.USER);
        user.setStandardModelUriId(settings.getStandardModelUri().getId());
        user.setAiCanUpdateMemory(true);
        user.setMemoryEnabled(true);
        user.setMoney(settings.getStartMoneyRub());
        user.setMemory(""); // Пустая память при создании

        return toDto(repoUser.save(user));
    }

    public AppUserDto findByUidFirebase(String uidFirebase) {
        return toDto(repoUser.findByUidFirebase(uidFirebase)
                .orElseThrow(() -> new GlobalException("USER_NOT_FOUND", "Пользователь с uidFirebase " + uidFirebase + " не найден.")));
    }

    // Вспомогательный метод, если нужно найти пользователя по ID
    public AppUserDto findById(Long userId) {
        return toDto(repoUser.findById(userId)
                .orElseThrow(() -> new GlobalException("USER_NOT_FOUND", "Пользователь с ID " + userId + " не найден.")));
    }


    /* ------- маппинг -------- */
    private AppUserDto toDto(AppUser e) {
        if (e == null) return null;
        return AppUserDto.builder()
                .id(e.getId())
                .email(e.getEmail())
                .uidFirebase(e.getUidFirebase())
                .role(e.getRole().name())
                .standardModelUriId(e.getStandardModelUriId())
                .money(e.getMoney().doubleValue())
                .memory(e.getMemory())
                .memoryEnabled(e.isMemoryEnabled())
                .aiCanUpdateMemory(e.isAiCanUpdateMemory())
                .dateCreate(e.getDateCreate())
                .build();
    }

    private AppUser fromDto(AppUserDto d) {
        if (d == null) return null;
        AppUser.AppUserBuilder builder = AppUser.builder()
                .id(d.getId())
                .email(d.getEmail())
                .uidFirebase(d.getUidFirebase())
                .standardModelUriId(d.getStandardModelUriId())
                .money(BigDecimal.valueOf(d.getMoney()).setScale(4, RoundingMode.HALF_UP))
                .memory(d.getMemory())
                .memoryEnabled(d.isMemoryEnabled())
                .aiCanUpdateMemory(d.isAiCanUpdateMemory());

        return builder.build();
    }
}