package com.promts.promts_test_server.promts_generator.core.dto.inbound;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class RequestGeneratorDTO {

    private String model;
    private List<Message> messages;
    private double temperature;
    private String context;


    @Getter
    @Setter
    @ToString
    public static class Message {
        private String role;
        private String text;
    }
}
