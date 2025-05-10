package com.promts.promts_test_server.promts_backend.dto.User.inbound;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdateUserRequestDTO {
    private boolean memoryEnabled;
    private boolean aiCanUpdateMemory;
    private String memory;
    private Long standardModelUriId;
}
