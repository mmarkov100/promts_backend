package com.promts.promts_test_server.promts_generator.features.openrouter.dto.inbound;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class OpenAIo4MiniHighResponseDTO {

    private List<Choice> choices;
    private Usage usage;


    @Getter
    @Setter
    @ToString
    public static class Choice{

        private OpenAIGPT4oMiniResponseDTO.Choice.Message message;

        @Getter
        @Setter
        @ToString
        public static class Message{

            private String role;
            private String content;
        }
    }

    @Getter
    @Setter
    @ToString
    public static class Usage{
        private Integer prompt_tokens;
        private Integer completion_tokens;
        private Integer total_tokens;
    }
}
