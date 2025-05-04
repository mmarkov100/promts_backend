package com.promts.promts_test_server.dto.Chat.inbound;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdateChatSettingsDTO {
    private Long chatId;
    private Long modelUriId;
    private double temperature;
    private String context;
    private boolean useMemory;
    private boolean updateMemory;
    private boolean starredChat;
}
