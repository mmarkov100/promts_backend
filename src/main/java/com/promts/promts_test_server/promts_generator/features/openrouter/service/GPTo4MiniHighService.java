package com.promts.promts_test_server.promts_generator.features.openrouter.service;

import com.promts.promts_test_server.promts_db.entity.modeluri.ModelUri;
import com.promts.promts_test_server.promts_db.service.modeluri.ModelUriService;
import com.promts.promts_test_server.promts_generator.core.dto.inbound.RequestGeneratorDTO;
import com.promts.promts_test_server.promts_generator.core.dto.outbound.ResponseDTO;
import com.promts.promts_test_server.promts_generator.features.openrouter.dto.inbound.OpenAIo4MiniHighResponseDTO;
import com.promts.promts_test_server.promts_generator.features.openrouter.dto.outbound.OpenAIo4MiniHighDTO;
import com.promts.promts_test_server.promts_generator.features.openrouter.repository.ChatCompletionRepository;
import com.promts.promts_test_server.shared.exception.GlobalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GPTo4MiniHighService {

    private static final Logger logger = LoggerFactory.getLogger(GPTo4MiniHighService.class);

    private final ChatCompletionRepository repository;
    private final ModelUriService modelService;

    @Autowired
    public GPTo4MiniHighService(ChatCompletionRepository repository, ModelUriService modelService) {
        this.repository = repository;
        this.modelService = modelService;
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
        if (in.getContext() != null && !in.getContext().isBlank()) {
            msgs.add(0, new OpenAIo4MiniHighDTO.Message(
                    "system",
                    List.of(new OpenAIo4MiniHighDTO.Message.Content("text", in.getContext(), null))
            ));
        }
        outbound.setMessages(msgs);

        // 2. Отправляем запрос в OpenRouter
        OpenAIo4MiniHighResponseDTO raw ;
        try {
            raw = repository.post(
                    outbound,
                    OpenAIo4MiniHighResponseDTO.class
            );
        } catch (Exception e) {
            logger.error("Ошибка при вызове repository.post: ", e);
            throw new GlobalException("OPENROUTER_REQUEST_FAILED", "Ошибка при отправке запроса в OpenRouter: " + e.getMessage());
        }

        if (raw == null) {
            logger.error("Получен null ответ от OpenRouter.");
            throw new GlobalException("OPENROUTER_NULL_RESPONSE", "Получен null ответ от OpenRouter");
        }

        if (raw.getChoices() == null || raw.getChoices().isEmpty()) {
            logger.error("Список 'choices' в ответе OpenRouter пуст или null. Response: {}", raw);
            // Здесь можно проверить, есть ли в raw поле с ошибкой от OpenRouter и передать его
            throw new GlobalException("OPENROUTER_NO_CHOICES", "Ответ от OpenRouter не содержит вариантов ('choices').");
        }

        // 3. Маппим ответ обратно в ваш универсальный ResponseDTO
        OpenAIo4MiniHighResponseDTO.Choice choice = raw.getChoices().get(0);

        if (choice == null || choice.getMessage() == null) {
            logger.error("'choice' или 'choice.message' в ответе OpenRouter null. Choice: {}", choice);
            throw new GlobalException("OPENROUTER_NO_MESSAGE_IN_CHOICE", "Ответ от OpenRouter не содержит 'message' в 'choice'.");
        }
        if (choice.getMessage().getContent() == null) {
            logger.error("'choice.message.content' в ответе OpenRouter null. Message: {}", choice.getMessage());
            throw new GlobalException("OPENROUTER_NO_CONTENT_IN_MESSAGE", "Ответ от OpenRouter не содержит 'content' в 'message'.");
        }

        ResponseDTO.MessageDTO message = new ResponseDTO.MessageDTO(
                choice.getMessage().getRole(),
                choice.getMessage().getContent()
        );

        ModelUri modelUri = modelService.findBySystemName(in.getModel())
                .orElseThrow(() -> new GlobalException("MODEL_NOT_FOUND","Модель не найдена"));

        if (raw.getUsage() == null) {
            logger.error("'usage' в ответе OpenRouter null. Response: {}", raw);
            throw new GlobalException("OPENROUTER_NO_USAGE", "Ответ от OpenRouter не содержит информации об использовании ('usage').");
        }

        OpenAIo4MiniHighResponseDTO.Usage u = raw.getUsage();

        double totalCost = 0.0;
        if (u.getCompletion_tokens()!=null)
            totalCost += modelUri.getCostOutputTokensRub().doubleValue()*u.getCompletion_tokens()/1000;
        if (u.getPrompt_tokens()!=null)
            totalCost += modelUri.getCostInputTokensRub().doubleValue()*u.getPrompt_tokens()/1000;

        ResponseDTO.UsageDTO usage = new ResponseDTO.UsageDTO(
                u.getTotal_tokens() != null ? u.getTotal_tokens()  : 0,
                u.getCompletion_tokens() != null ?  u.getCompletion_tokens() : 0,
                u.getPrompt_tokens() != null ? u.getPrompt_tokens()  : 0,
                totalCost
        );
        return new ResponseDTO(message, usage);
    }
}
