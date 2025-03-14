package com.promts.promts_test_server.dto.Chat.inbound;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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

