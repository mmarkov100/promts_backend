package com.promts.promts_test_server.promts_db.dto.user.outbound;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppUserDto {
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