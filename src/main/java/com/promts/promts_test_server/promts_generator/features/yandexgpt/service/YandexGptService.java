package com.promts.promts_test_server.promts_generator.features.yandexgpt.service;

import com.promts.promts_test_server.promts_db.entity.modeluri.ModelUri;
import com.promts.promts_test_server.promts_db.service.modeluri.ModelUriService;
import com.promts.promts_test_server.promts_generator.core.dto.inbound.RequestGeneratorDTO;
import com.promts.promts_test_server.promts_generator.core.dto.outbound.ResponseDTO;
import com.promts.promts_test_server.promts_generator.features.yandexgpt.config.YandexConfig;
import com.promts.promts_test_server.promts_generator.features.yandexgpt.dto.YandexGptRequest;
import com.promts.promts_test_server.promts_generator.features.yandexgpt.dto.YandexGptResponse;
import com.promts.promts_test_server.promts_generator.features.yandexgpt.repository.ChatYandexCompletionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class YandexGptService {

    private final ChatYandexCompletionRepository repository;
    private final ModelUriService modelService;   // для стоимости
    private final YandexConfig yandexConfig;

    public ResponseDTO serviceMessage(RequestGeneratorDTO in) {

        /* 1. Маппинг входа */
        YandexGptRequest out = new YandexGptRequest();
        out.setModelUri("gpt://" + yandexConfig.getFolderId()
                + "/yandexgpt");

        YandexGptRequest.CompletionOptions co = new YandexGptRequest.CompletionOptions();
        co.setTemperature(in.getTemperature());
        co.setMaxTokens("2000");
        out.setCompletionOptions(co);

        /* system-context как первое сообщение */
        List<YandexGptRequest.Message> msgs = new ArrayList<>();
        if (in.getContext()!=null && !in.getContext().isBlank()){
            msgs.add(new YandexGptRequest.Message("system", in.getContext()));
        }
        in.getMessages().forEach(m -> msgs.add(
                new YandexGptRequest.Message(m.getRole(), m.getText())));
        out.setMessages(msgs);

        /* 2. Вызов YandexGPT */
        YandexGptResponse raw = repository.post(out, YandexGptResponse.class);

        /* 3. Маппинг ответа */
        YandexGptResponse.Result.Alternative alt = raw.getResult().getAlternatives().get(0);
        ResponseDTO.MessageDTO message = new ResponseDTO.MessageDTO(
                alt.getMessage().getRole(), alt.getMessage().getText());

        /* 4. Стоимость ― берём тарифы из model_uri */
        ModelUri mu = modelService.findBySystemName(in.getModel()).orElseThrow();
        YandexGptResponse.Result.Usage u = raw.getResult().getUsage();
        double cost = 0.0;
        cost += mu.getCostInputTokensRub().doubleValue()  * u.getInputTextTokens() / 1000.0;
        cost += mu.getCostOutputTokensRub().doubleValue() * u.getCompletionTokens() / 1000.0;
        cost += Optional.ofNullable(mu.getCostRequestRub()).orElse(BigDecimal.ZERO).doubleValue();

        ResponseDTO.UsageDTO usage = new ResponseDTO.UsageDTO(
                u.getTotalTokens(), u.getCompletionTokens(), u.getInputTextTokens(), cost);

        return new ResponseDTO(message, usage);
    }
}
