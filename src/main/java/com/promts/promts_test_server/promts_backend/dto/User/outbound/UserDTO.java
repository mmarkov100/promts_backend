package com.promts.promts_test_server.promts_backend.dto.User.outbound;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long id;
    private String email;
    private String role;
    private double money;
    private String memory;
    private boolean memoryEnabled;
    private boolean aiCanUpdateMemory;
    private Long standardModelUriId;
}
