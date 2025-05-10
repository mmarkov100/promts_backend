package com.promts.promts_test_server.promts_generator.features.openrouter.service;

import com.promts.promts_test_server.promts_generator.core.dto.inbound.RequestGeneratorDTO;
import com.promts.promts_test_server.promts_generator.core.dto.outbound.ResponseDTO;
import com.promts.promts_test_server.promts_generator.features.openrouter.dto.inbound.OpenAIGPT4oMiniResponseDTO;
import com.promts.promts_test_server.promts_generator.features.openrouter.dto.outbound.OpenAIGPT4oMiniDTO;
import com.promts.promts_test_server.promts_generator.features.openrouter.repository.ChatCompletionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GPT4oMiniService {

    private static final Logger logger = LoggerFactory.getLogger(GPT4oMiniService.class);

    private final ChatCompletionRepository repository;

    @Autowired
    public GPT4oMiniService(ChatCompletionRepository repository) {
        this.repository = repository;
    }

    public ResponseDTO serviceMessage(RequestGeneratorDTO in) {

        // 1. Маппим входящую DTO в формат OpenRouter
        OpenAIGPT4oMiniDTO outbound = new OpenAIGPT4oMiniDTO();
        outbound.setModel(in.getModel());
        outbound.setTemperature(in.getTemperature());
        List<OpenAIGPT4oMiniDTO.Message> msgs = in.getMessages().stream()
                .map(m -> {
                    OpenAIGPT4oMiniDTO.Message outMsg = new OpenAIGPT4oMiniDTO.Message();
                    outMsg.setRole(m.getRole());
                    // каждое сообщение — список контентов с типом text
                    outMsg.setContent(List.of(
                            new OpenAIGPT4oMiniDTO.Message.Content("text", m.getText(), null)
                    ));
                    return outMsg;
                })
                .collect(Collectors.toList());
        if (!in.getContext().isEmpty()) {
            msgs.add(0, new OpenAIGPT4oMiniDTO.Message("system", List.of(
                    new OpenAIGPT4oMiniDTO.Message.Content("text", in.getContext(), null))));
        }
        outbound.setMessages(msgs);

        // 2. Отправляем запрос в OpenRouter
        OpenAIGPT4oMiniResponseDTO raw = repository.post(
                outbound,
                OpenAIGPT4oMiniResponseDTO.class
        );

        // 3. Маппим ответ обратно в ваш универсальный ResponseDTO
        OpenAIGPT4oMiniResponseDTO.Choice choice = raw.getChoices().get(0);
        ResponseDTO.MessageDTO message = new ResponseDTO.MessageDTO(
                choice.getMessage().getRole(),
                choice.getMessage().getContent()
        );

        OpenAIGPT4oMiniResponseDTO.Usage u = raw.getUsage();
        ResponseDTO.UsageDTO usage = new ResponseDTO.UsageDTO(
                u.getTotal_tokens(),
                u.getCompletion_tokens(),
                u.getPrompt_tokens(),
                0.0 // сюда можно подставить расчёт стоимости, если нужно
        );
        return new ResponseDTO(message, usage);
    }
}
