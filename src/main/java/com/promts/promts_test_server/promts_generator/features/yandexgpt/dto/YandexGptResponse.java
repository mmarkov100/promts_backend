package com.promts.promts_test_server.promts_generator.features.yandexgpt.dto;

import lombok.Data;
import java.util.List;

@Data
public class YandexGptResponse {
    private Result result;

    @Data
    public static class Result {
        private List<Alternative> alternatives;
        private Usage usage;
        private String modelVersion;

        @Data
        public static class Alternative {
            private Message message;
            private String status;

            @Data
            public static class Message {
                private String role;
                private String text;
            }
        }

        @Data
        public static class Usage {
            private int inputTextTokens;
            private int completionTokens;
            private int totalTokens;
        }
    }
}
