package com.promts.promts_test_server.dto.ChatBot.inbound;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class  CreateChatBotDTO {
    private String chatBotName;
    private String chatBotDesc;
    private String helloMessage;
    private Long modelUriId;
    private double temperature;
    private String context;
    private boolean canUseMemory;
    private boolean canUpdateMemory;
    private boolean canEditModelUri;
    private boolean canEditContext;
}
