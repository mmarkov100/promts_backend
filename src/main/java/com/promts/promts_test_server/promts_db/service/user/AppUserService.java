package com.promts.promts_test_server.promts_db.service.user;

import com.promts.promts_test_server.promts_db.dto.user.outbound.AppUserDto;
import com.promts.promts_test_server.promts_db.entity.app_settings.AppSettings;
import com.promts.promts_test_server.promts_db.entity.user.AppUser;
import com.promts.promts_test_server.promts_db.repository.AppSettingsRepository;
import com.promts.promts_test_server.promts_db.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
@RequiredArgsConstructor
public class AppUserService {

    private final AppUserRepository repoUser;
    private final AppSettingsRepository repoSettings;

    public AppUserDto updateUserSettings(AppUserDto dto) {
        AppUser userDTO = fromDto(dto);
        AppUser user = repoUser.findByUidFirebase(dto.getUidFirebase()).orElseThrow();

        user.setMemory(userDTO.getMemory());
        user.setMemoryEnabled(userDTO.isMemoryEnabled());
        user.setAiCanUpdateMemory(userDTO.isAiCanUpdateMemory());
        user.setStandardModelUriId(userDTO.getStandardModelUriId());

        return toDto(repoUser.save(user));
    }

    public AppUserDto createUser(AppUserDto dto) {
        AppUser user = fromDto(dto);
        AppSettings settings = repoSettings.findById(0L).orElseThrow();

        user.setStandardModelUriId(settings.getStandardModelUri().getId());
        user.setAiCanUpdateMemory(true);
        user.setMemoryEnabled(true);
        user.setMoney(settings.getStartMoneyRub());
        user.setMemory("");

        return toDto(repoUser.save(user));
    }

    public AppUserDto findByUidFirebase(String uidFirebase) {
        return toDto(repoUser.findByUidFirebase(uidFirebase).orElseThrow());
    }

    /* ------- маппинг -------- */
    private AppUserDto toDto(AppUser e) {
        return AppUserDto.builder()
                .id(e.getId())
                .email(e.getEmail())
                .uidFirebase(e.getUidFirebase())
                .role(e.getRole().name())
                .standardModelUriId(e.getStandardModelUriId())
                .money(e.getMoney().toBigInteger().doubleValue())
                .memory(e.getMemory())
                .memoryEnabled(e.isMemoryEnabled())
                .aiCanUpdateMemory(e.isAiCanUpdateMemory())
                .dateCreate(e.getDateCreate())
                .build();
    }

    private AppUser fromDto(AppUserDto d) {
        return AppUser.builder()
                .id(d.getId())
                .email(d.getEmail())
                .uidFirebase(d.getUidFirebase())
                .standardModelUriId(d.getStandardModelUriId())
                .money(BigDecimal.valueOf(d.getMoney()))
                .memory(d.getMemory())
                .memoryEnabled(d.isMemoryEnabled())
                .aiCanUpdateMemory(d.isAiCanUpdateMemory())
                .build();
    }
}
