package com.promts.promts_test_server.promts_generator.features.openrouter.dto.outbound;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OpenAIo4MiniHighDTO {

    private String model;
    private double temperature;
    private List<Message> messages;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class Message{

        private String role;
        private List<Content> content;

        @Getter
        @Setter
        @AllArgsConstructor
        @NoArgsConstructor
        @ToString
        public static class Content{

            private String type;
            private String text;
            private String image_url;
        }
    }
}
