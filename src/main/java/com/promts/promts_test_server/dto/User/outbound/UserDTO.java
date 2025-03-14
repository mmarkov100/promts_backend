package com.promts.promts_test_server.dto.User.outbound;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
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
