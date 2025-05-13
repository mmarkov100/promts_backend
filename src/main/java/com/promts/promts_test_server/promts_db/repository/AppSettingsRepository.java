package com.promts.promts_test_server.promts_db.repository;

import com.promts.promts_test_server.promts_db.entity.app_settings.AppSettings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppSettingsRepository extends JpaRepository<AppSettings, Long> {
}
