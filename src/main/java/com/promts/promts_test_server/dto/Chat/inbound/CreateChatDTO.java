package com.promts.promts_test_server.dto.Chat.inbound;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateChatDTO {
    private Long modelUriId;
    private double temperature;
    private String context;
    private boolean useMemory;
    private boolean updateMemory;
}
