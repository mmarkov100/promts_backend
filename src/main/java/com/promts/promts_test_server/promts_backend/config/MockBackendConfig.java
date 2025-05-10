package com.promts.promts_test_server.promts_backend.config;

import lombok.Getter;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class MockBackendConfig {

    private final long delay = 3000;
    private final long userId = 1;
    private final String urlGenerator = "http://localhost:8090";
    private final String urlDatabase = "http://localhost:8090";
}
