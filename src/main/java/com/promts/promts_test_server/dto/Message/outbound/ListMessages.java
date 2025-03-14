package com.promts.promts_test_server.dto.Message.outbound;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ListMessages {

    private List<Message> messages;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Message{
        private Long id;
        private String text;
        private String role;
        private LocalDateTime dateCreate;
    }
}
