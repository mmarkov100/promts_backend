package com.promts.promts_test_server.dto.Chat.outbound;

import com.promts.promts_test_server.dto.User.outbound.GetUserResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatListShortDTO {
    private List<ChatList> chats;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatList {
        private Long id;
        private String chatName;
        private boolean starredChat;
        private LocalDateTime dateEdit;
    }
}
