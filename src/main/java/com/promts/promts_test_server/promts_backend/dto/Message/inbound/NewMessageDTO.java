package com.promts.promts_test_server.promts_backend.dto.Message.inbound;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NewMessageDTO {
    private String text;
    private Long modelUriId;
    private Long chatId;
}
