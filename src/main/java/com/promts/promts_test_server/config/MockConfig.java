package com.promts.promts_test_server.config;

import lombok.Getter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("mock")
@Configuration
@Getter
public class MockConfig {

    private final long delay = 0;
}
