package com.promts.promts_test_server.dto.ChatBot.outbound;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatBotResponseDTO {

    private Long id;
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
    private int messagesToday;
    private int totalMessages;
}
