package com.promts.promts_test_server.promts_db.service.settings;

import com.promts.promts_test_server.promts_db.entity.app_settings.AppSettings;
import com.promts.promts_test_server.promts_db.repository.AppSettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppSettingsService {

    private final AppSettingsRepository repo;

    /** Всегда возвращаем единственную строку (id=1). */
    public AppSettings get() {
        return repo.findById(1L).orElseThrow();
    }

    public AppSettings update(AppSettings newSettings) {
        newSettings.setId(1L);
        return repo.save(newSettings);
    }
}
