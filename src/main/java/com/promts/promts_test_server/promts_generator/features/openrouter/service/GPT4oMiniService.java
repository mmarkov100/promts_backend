package com.promts.promts_test_server.promts_generator.features.openrouter.service;

import com.promts.promts_test_server.promts_db.entity.modeluri.ModelUri;
import com.promts.promts_test_server.promts_db.service.modeluri.ModelUriService;
import com.promts.promts_test_server.promts_generator.core.dto.inbound.RequestGeneratorDTO;
import com.promts.promts_test_server.promts_generator.core.dto.outbound.ResponseDTO;
import com.promts.promts_test_server.promts_generator.features.openrouter.dto.inbound.OpenAIGPT4oMiniResponseDTO;
import com.promts.promts_test_server.promts_generator.features.openrouter.dto.outbound.OpenAIGPT4oMiniDTO;
import com.promts.promts_test_server.promts_generator.features.openrouter.repository.ChatCompletionRepository;
import com.promts.promts_test_server.shared.exception.GlobalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GPT4oMiniService {

    private static final Logger logger = LoggerFactory.getLogger(GPT4oMiniService.class);

    private final ChatCompletionRepository repository;
    private final ModelUriService modelService;

    @Autowired
    public GPT4oMiniService(ChatCompletionRepository repository, ModelUriService modelService) {
        this.repository = repository;
        this.modelService = modelService;
    }

    public ResponseDTO serviceMessage(RequestGeneratorDTO in) {

        logger.info("Получен запрос: {}", in.toString()); // Логируем входящий запрос целиком
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

        // Добавляем системное сообщение (контекст) в начало списка, если оно есть
        if (in.getContext() != null && !in.getContext().isEmpty()) {
            msgs.add(0, new OpenAIGPT4oMiniDTO.Message("system", List.of(
                    new OpenAIGPT4oMiniDTO.Message.Content("text", in.getContext(), null))));
        }
        outbound.setMessages(msgs);

        logger.info("Сформирован запрос для OpenRouter: {}", outbound);

        // 2. Отправляем запрос в OpenRouter
        OpenAIGPT4oMiniResponseDTO raw;
        try {
            raw = repository.post(
                    outbound,
                    OpenAIGPT4oMiniResponseDTO.class
            );
        } catch (Exception e) {
            logger.error("Ошибка при вызове repository.post: ", e);
            throw new GlobalException("OPENROUTER_REQUEST_FAILED", "Ошибка при отправке запроса в OpenRouter: " + e.getMessage());
        }

        logger.info("Запрос отправлен и получен ответ. Raw response: {}", (raw != null ? raw.toString() : "null"));

        if (raw == null) {
            logger.error("Получен null ответ от OpenRouter.");
            throw new GlobalException("OPENROUTER_NULL_RESPONSE", "Получен null ответ от OpenRouter");
        }

        if (raw.getChoices() == null || raw.getChoices().isEmpty()) {
            logger.error("Список 'choices' в ответе OpenRouter пуст или null. Response: {}", raw);
            // Здесь можно проверить, есть ли в raw поле с ошибкой от OpenRouter и передать его
            throw new GlobalException("OPENROUTER_NO_CHOICES", "Ответ от OpenRouter не содержит вариантов ('choices').");
        }

        OpenAIGPT4oMiniResponseDTO.Choice choice = raw.getChoices().get(0);
        logger.info("Выбран choice: {}", (choice != null ? choice.toString() : "null"));

        if (choice == null || choice.getMessage() == null) {
            logger.error("'choice' или 'choice.message' в ответе OpenRouter null. Choice: {}", choice);
            throw new GlobalException("OPENROUTER_NO_MESSAGE_IN_CHOICE", "Ответ от OpenRouter не содержит 'message' в 'choice'.");
        }
        if (choice.getMessage().getContent() == null) {
            logger.error("'choice.message.content' в ответе OpenRouter null. Message: {}", choice.getMessage());
            throw new GlobalException("OPENROUTER_NO_CONTENT_IN_MESSAGE", "Ответ от OpenRouter не содержит 'content' в 'message'.");
        }


        // 3. Маппим ответ обратно в ваш универсальный ResponseDTO
        ResponseDTO.MessageDTO message = new ResponseDTO.MessageDTO(
                choice.getMessage().getRole(),
                choice.getMessage().getContent()
        );
        logger.info("Маппим ответ. MessageDTO создан: {}", message);

        if (raw.getUsage() == null) {
            logger.error("'usage' в ответе OpenRouter null. Response: {}", raw);
            throw new GlobalException("OPENROUTER_NO_USAGE", "Ответ от OpenRouter не содержит информации об использовании ('usage').");
        }
        OpenAIGPT4oMiniResponseDTO.Usage u = raw.getUsage();
        logger.info("Получена информация 'usage'");

        // Расчет стоимости
        double calculatedCost = 0.0;
        // Пытаемся найти модель по системному имени, которое пришло в запросе `in.getModel()`
        Optional<ModelUri> modelUriOpt = modelService.findBySystemName(in.getModel());

        if (modelUriOpt.isPresent()) {
            ModelUri modelUri = modelUriOpt.get();
            logger.info("Информация о модели для расчета стоимости: {}", modelUri.getName());
            if (u.getCompletion_tokens() != null && modelUri.getCostOutputTokensRub() != null) {
                calculatedCost += modelUri.getCostOutputTokensRub().doubleValue() * u.getCompletion_tokens() / 1000.0;
            }
            if (u.getPrompt_tokens() != null && modelUri.getCostInputTokensRub() != null) {
                calculatedCost += modelUri.getCostInputTokensRub().doubleValue() * u.getPrompt_tokens() / 1000.0;
            }
            if (modelUri.getCostRequestRub() != null) {
                calculatedCost += modelUri.getCostRequestRub().doubleValue();
            }
            logger.info("Рассчитанная стоимость: {}", calculatedCost);
        } else {
            logger.warn("Модель с системным именем '{}' не найдена в базе. Стоимость не будет рассчитана.", in.getModel());
        }


        Integer totalTokens = u.getTotal_tokens();
        Integer completionTokens = u.getCompletion_tokens();
        Integer promptTokens = u.getPrompt_tokens();

        ResponseDTO.UsageDTO usage = new ResponseDTO.UsageDTO(
                totalTokens != null ? totalTokens : 0,
                completionTokens != null ? completionTokens : 0,
                promptTokens != null ? promptTokens : 0,
                calculatedCost // Используем рассчитанную стоимость
        );
        logger.info("Сформирован UsageDTO");
        return new ResponseDTO(message, usage);
    }
}