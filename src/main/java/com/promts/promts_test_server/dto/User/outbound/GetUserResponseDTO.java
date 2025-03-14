package com.promts.promts_test_server.dto.User.outbound;

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
public class GetUserResponseDTO {

    private User user;
    private List<ChatList> chats;
    private List<NeuralNetwork> neuralNetworks;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class User{
        private Long id;
        private String email;
        private String role;
        private double money;
        private String memory;
        private boolean memoryEnabled;
        private boolean aiCanUpdateMemory;
        private Long standardModelUriId;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatList{
        private Long id;
        private String chatName;
        private boolean starredChat;
        private LocalDateTime dateEdit;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NeuralNetwork {
        private Long id;
        private String name;
        private String systemName;
        private String desc;
    }
}
