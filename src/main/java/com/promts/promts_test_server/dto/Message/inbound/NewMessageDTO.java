package com.promts.promts_test_server.dto.Message.inbound;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewMessageDTO {
    private String text;
    private Long modelUriId;
    private Long chatId;
}
