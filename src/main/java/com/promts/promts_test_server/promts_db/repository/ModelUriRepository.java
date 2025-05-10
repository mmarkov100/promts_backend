package com.promts.promts_test_server.promts_db.repository;

import com.promts.promts_test_server.promts_db.entity.modeluri.ModelUri;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ModelUriRepository extends JpaRepository<ModelUri, Long> {
    Optional<ModelUri> findBySystemName(String systemName);
}
