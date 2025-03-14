package com.promts.promts_test_server.dto.Message.outbound;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SuccessDeleteMessagesDTO {
    private boolean success;
    private String message;
}
