package com.promts.promts_test_server.dto.ChatBot.outbound;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatBotsResponseDTO {
    private List<ChatBotShort> chatBots;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChatBotShort{
        private Long id;
        private String chatBotName;
        private String chatBotDesc;
        private int messagesToday;
        private int totalMessages;
        private boolean isSelectedByRedact;
    }
}
