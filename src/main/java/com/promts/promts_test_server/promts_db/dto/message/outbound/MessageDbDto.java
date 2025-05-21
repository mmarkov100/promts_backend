package com.promts.promts_test_server.promts_db.dto.message.outbound;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageDbDto {
    private Long id;
    private Long chatId;
    private Long modelUriId;
    private boolean oldMessage;
    private String role;
    private String text;
    private String type;
    private LocalDateTime dateCreate;
}
