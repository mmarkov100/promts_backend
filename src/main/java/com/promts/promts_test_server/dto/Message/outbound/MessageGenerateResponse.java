package com.promts.promts_test_server.dto.Message.outbound;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageGenerateResponse {

    private MessageRequest messageRequest;
    private User user;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MessageRequest{
        private Long id;
        private String text;
        private LocalDateTime dateCreate;
        private boolean stopped;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class User{
        private boolean memoryUpdated;
        private String newMemory;
        private double cost;
        private double money;
    }
}
