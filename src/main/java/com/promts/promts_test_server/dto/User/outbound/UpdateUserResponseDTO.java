package com.promts.promts_test_server.dto.User.outbound;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserResponseDTO {
    private boolean updated;
    private boolean memoryEnabled;
    private boolean aiCanUpdateMemory;
    private String memory;
    private Long standardModelUriId;
}
