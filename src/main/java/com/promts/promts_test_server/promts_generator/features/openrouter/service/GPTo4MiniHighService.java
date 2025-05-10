package com.promts.promts_test_server.promts_generator.features.openrouter.service;

import com.promts.promts_test_server.promts_generator.core.dto.inbound.RequestGeneratorDTO;
import com.promts.promts_test_server.promts_generator.core.dto.outbound.ResponseDTO;
import com.promts.promts_test_server.promts_generator.features.openrouter.dto.inbound.OpenAIo4MiniHighResponseDTO;
import com.promts.promts_test_server.promts_generator.features.openrouter.dto.outbound.OpenAIo4MiniHighDTO;
import com.promts.promts_test_server.promts_generator.features.openrouter.repository.ChatCompletionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GPTo4MiniHighService {

    private final ChatCompletionRepository repository;

    private static final Logger logger = LoggerFactory.getLogger(GPTo4MiniHighService.class);

    @Autowired
    public GPTo4MiniHighService(ChatCompletionRepository repository) {
        this.repository = repository;
    }

    public ResponseDTO serviceMessage(RequestGeneratorDTO in) {

        // 1. Маппим входящую DTO в формат OpenRouter
        OpenAIo4MiniHighDTO outbound = new OpenAIo4MiniHighDTO();
        outbound.setModel(in.getModel());
        outbound.setTemperature(in.getTemperature());
        List<OpenAIo4MiniHighDTO.Message> msgs = in.getMessages().stream()
                .map(m -> {
                    OpenAIo4MiniHighDTO.Message outMsg = new OpenAIo4MiniHighDTO.Message();
                    outMsg.setRole(m.getRole().toLowerCase());
                    // каждое сообщение — список контентов с типом text
                    outMsg.setContent(List.of(
                            new OpenAIo4MiniHighDTO.Message.Content("text", m.getText(), null)
                    ));
                    return outMsg;
                })
                .collect(Collectors.toList());
        if (!in.getContext().isEmpty()) {
            msgs.add(0, new OpenAIo4MiniHighDTO.Message("system", List.of(
                    new OpenAIo4MiniHighDTO.Message.Content("text", in.getContext(), null))));
        }
        outbound.setMessages(msgs);

        // 2. Отправляем запрос в OpenRouter
        OpenAIo4MiniHighResponseDTO raw = repository.post(
                outbound,
                OpenAIo4MiniHighResponseDTO.class
        );

        // 3. Маппим ответ обратно в ваш универсальный ResponseDTO
        OpenAIo4MiniHighResponseDTO.Choice choice = raw.getChoices().get(0);
        ResponseDTO.MessageDTO message = new ResponseDTO.MessageDTO(
                choice.getMessage().getRole(),
                choice.getMessage().getContent()
        );

        OpenAIo4MiniHighResponseDTO.Usage u = raw.getUsage();
        ResponseDTO.UsageDTO usage = new ResponseDTO.UsageDTO(
                u.getTotal_tokens(),
                u.getCompletion_tokens(),
                u.getPrompt_tokens(),
                0.0 // сюда можно подставить расчёт стоимости, если нужно
        );
        return new ResponseDTO(message, usage);
    }
}
