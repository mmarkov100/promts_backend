package com.promts.promts_test_server.promts_backend.dto.Chat.inbound;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ChatModelDTO {
    private Long id;
    private Long userId;
    private Long chatBotId;
    private String chatName;
    private Long modelUriId;
    private double temperature;
    private String context;
    private boolean starredChat;
    private boolean useMemory;
    private boolean updateMemory;
    private boolean canUseMemory;
    private boolean canUpdateMemory;
    private boolean canEditModelUri;
    private boolean canEditContext;
    private LocalDateTime dateEdit;
    private LocalDateTime dateCreate;
}

