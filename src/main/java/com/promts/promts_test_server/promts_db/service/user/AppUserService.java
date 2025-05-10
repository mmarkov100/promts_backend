package com.promts.promts_test_server.promts_db.service.user;

import com.promts.promts_test_server.promts_db.dto.user.outbound.AppUserDto;
import com.promts.promts_test_server.promts_db.entity.user.AppUser;
import com.promts.promts_test_server.promts_db.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
@RequiredArgsConstructor
public class AppUserService {

    private final AppUserRepository repo;

    public AppUserDto save(AppUserDto dto) {
        AppUser user = fromDto(dto);

        return toDto(repo.save(user));
    }

    public AppUserDto create(AppUserDto dto) {
        AppUser user = fromDto(dto);

        return toDto(repo.save(user));
    }

    public AppUserDto findByUidFirebase(String uidFirebase) {
        return toDto(repo.findByUidFirebase(uidFirebase).orElseThrow());
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
