package com.promts.promts_test_server.dto.Generator.outbound;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestGenerateMessageDTO {

    private List<Message> messages;
    private double temperature;

    @Getter
    @Setter
    @Data
    public static class Message {

        private String role;
        private List<Content> content;

        @Getter
        @Setter
        @Data
        public static class Content {
            private String type;
            private String text;
        }
    }
}
