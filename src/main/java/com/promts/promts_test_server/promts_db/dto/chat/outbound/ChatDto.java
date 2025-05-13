package com.promts.promts_test_server.promts_db.dto.chat.outbound;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatDto {
    private Long chatId;
    private Long modelUriId;
    private Long chatBotId;
    private String chatName;
    private double temperature;
    private String context;
    private boolean useMemory;
    private boolean updateMemory;
    private boolean starredChat;
}
