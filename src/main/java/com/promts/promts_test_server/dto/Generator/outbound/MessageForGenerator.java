package com.promts.promts_test_server.dto.Generator.outbound;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageForGenerator {
    private String role;
    private String text;
}
