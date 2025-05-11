package com.promts.promts_test_server.promts_backend.dto.Message.inbound;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeleteMessagesDTO {
    private Long messageId;
    private Long modelUriId;
}
