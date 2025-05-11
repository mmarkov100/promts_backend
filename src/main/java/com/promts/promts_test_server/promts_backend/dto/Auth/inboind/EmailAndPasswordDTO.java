package com.promts.promts_test_server.promts_backend.dto.Auth.inboind;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EmailAndPasswordDTO {
    private String email;
    private String password;

}
