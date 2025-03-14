package com.promts.promts_test_server.dto.User.inbound;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoFromModelDTO {

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
        private String uidFirebase;
        private String role;
        private Long standardModelUriId;
        private double money;
        private String memory;
        private boolean memoryEnabled;
        private boolean aiCanUpdateMemory;
        private LocalDateTime dateCreate;
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
