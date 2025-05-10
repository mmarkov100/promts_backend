package com.promts.promts_test_server.promts_backend.dto.Shared.outbound;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SuccessMessageDTO {

    private boolean success;
    private String message;
}
