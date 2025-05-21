package com.promts.promts_test_server.promts_db.service.settings;

import com.promts.promts_test_server.promts_db.entity.app_settings.AppSettings;
import com.promts.promts_test_server.promts_db.repository.AppSettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppSettingsService {

    private final AppSettingsRepository repo;

    public AppSettings get() {
        return repo.findById(0L).orElseThrow();
    }

    public AppSettings update(AppSettings newSettings) {
        newSettings.setId(0L);
        return repo.save(newSettings);
    }
}
