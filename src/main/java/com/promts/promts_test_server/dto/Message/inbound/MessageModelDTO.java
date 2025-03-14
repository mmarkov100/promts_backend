package com.promts.promts_test_server.dto.Message.inbound;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageModelDTO {
    private Long id;
    private Long chatId;
    private Long modelUriId;
    private boolean oldMessage;
    private String role;
    private String text;
    private String type;
    private LocalDateTime dateCreate;
}
