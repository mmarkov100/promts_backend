package com.promts.promts_test_server.promts_generator.core.dto.outbound;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDTO {

    private MessageDTO message;
    private UsageDTO usage;

    @Setter
    @Getter
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MessageDTO {
        private String role;
        private String text;
    }

    @Setter
    @Getter
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UsageDTO {
        private int totalTokens;
        private int completionTokens;
        private int inputTextTokens;
        private double cost;

    }
}
