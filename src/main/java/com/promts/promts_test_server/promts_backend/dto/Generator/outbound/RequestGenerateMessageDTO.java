package com.promts.promts_test_server.promts_backend.dto.Generator.outbound;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestGenerateMessageDTO {

    private String model;
    private double temperature;
    private String context;
    private List<MessageForGenerator> messages;
}
