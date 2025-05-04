package com.promts.promts_test_server.dto.Auth.outbound;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SuccessAuthDTO {

    private boolean success;
    private String message;
}
