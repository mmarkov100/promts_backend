package com.promts.promts_test_server.dto.Auth.outbound;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SuccessAuthDTO {

    private boolean success;
    private String message;
}
