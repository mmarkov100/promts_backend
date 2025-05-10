package com.promts.promts_test_server.promts_db.dto.modeluri.outbound;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ModelUriDto {
    private Long id;
    private String name;
    private String systemName;
    private String desc;
}