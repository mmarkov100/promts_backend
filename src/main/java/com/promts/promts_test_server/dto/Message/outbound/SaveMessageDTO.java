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
public class SaveMessageDTO {
    private Long chatId;
    private Long modelUriId;
    private boolean oldMessage;
    private String role;
    private String text;
    private String type;
}
