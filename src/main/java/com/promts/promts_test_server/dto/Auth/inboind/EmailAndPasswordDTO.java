package com.promts.promts_test_server.dto.Auth.inboind;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailAndPasswordDTO {
    private String email;
    private String password;
}
