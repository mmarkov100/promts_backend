package com.promts.promts_test_server.dto.User.inbound;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserModelDTO {
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
