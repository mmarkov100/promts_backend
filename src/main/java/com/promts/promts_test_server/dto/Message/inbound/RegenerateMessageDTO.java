package com.promts.promts_test_server.dto.Message.inbound;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegenerateMessageDTO {
    private String messageId;
    private Long modelUriId;
}
