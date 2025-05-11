package com.promts.promts_test_server.promts_backend.dto.Message.outbound;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MessageGenerateResponse {

    private MessageRequest messageRequest;
    private User user;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
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
    @ToString
    public static class User{
        private boolean memoryUpdated;
        private String newMemory;
        private double cost;
        private double money;
    }
}
