package com.promts.promts_test_server.dto.ChatBot.inbound;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatBotModelDTO {

    private Long id;
    private Long userId;
    private String chatBotName;
    private String chatBotDesc;
    private String helloMessage;
    private Long modelUriId;
    private double temperature;
    private String context;
    private boolean isSelectedByRedact;
    private boolean canUseMemory;
    private boolean canUpdateMemory;
    private boolean canEditModelUri;
    private boolean canEditContext;
    private LocalDateTime dateEdit;
    private LocalDateTime dateCreate;
}
