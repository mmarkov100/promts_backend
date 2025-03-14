package com.promts.promts_test_server.dto.User.inbound;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequestDTO {
    private boolean memoryEnabled;
    private boolean aiCanUpdateMemory;
    private String memory;
    private Long standardModelUriId;
}
