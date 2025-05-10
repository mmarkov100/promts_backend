package com.promts.promts_test_server.promts_backend.dto.NeuralNetwork.outbound;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NeuralNetworkDTO {
    private Long id;
    private String name;
    private String systemName;
    private String desc;
}
