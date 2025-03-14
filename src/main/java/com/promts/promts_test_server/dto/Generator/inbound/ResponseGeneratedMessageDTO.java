package com.promts.promts_test_server.dto.Generator.inbound;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseGeneratedMessageDTO {

    private MessageDTO message;
    private UsageDTO usage;

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MessageDTO {

        private String role;
        private String text;
    }

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UsageDTO {

        private int totalTokens;
        private int completionTokens;
        private int inputTextTokens;
        private double cost;

    }
}
