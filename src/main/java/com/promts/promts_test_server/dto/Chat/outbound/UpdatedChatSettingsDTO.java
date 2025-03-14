package com.promts.promts_test_server.dto.Chat.outbound;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdatedChatSettingsDTO {
    private Long chatId;
    private Long modelUriId;
    private double temperature;
    private String context;
    private boolean useMemory;
    private boolean updateMemory;
}
