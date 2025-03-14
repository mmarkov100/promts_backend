package com.promts.promts_test_server.dto.NeuralNetwork.outbound;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NeuralNetworkDTO {
    private Long id;
    private String name;
    private String systemName;
    private String desc;
}
