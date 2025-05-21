package com.promts.promts_test_server.promts_backend.repository.Generator;

import com.promts.promts_test_server.promts_backend.config.MockBackendConfig;
import com.promts.promts_test_server.promts_backend.dto.Generator.inbound.ResponseGeneratedMessageDTO;
import com.promts.promts_test_server.promts_backend.dto.Generator.inbound.ResponseGeneratedVectorDTO;
import com.promts.promts_test_server.promts_backend.dto.Generator.outbound.MessageForGenerator;
import com.promts.promts_test_server.promts_backend.dto.Generator.outbound.RequestGenerateMessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Repository
@Profile({"neuro", "dbmock", "firebase"})
public class NeuroMockGeneratorRepository implements GeneratorRepository{

    private final RestTemplate restTemplate;
    private final HttpHeaders headers;
    private final String baseUrl;

    @Autowired
    public NeuroMockGeneratorRepository(RestTemplate restTemplate, MockBackendConfig mockBackendConfig) {
        this.restTemplate = restTemplate;
        this.headers = new HttpHeaders();
        this.baseUrl = mockBackendConfig.getUrlGenerator();
    }

    @Override
    public ResponseGeneratedMessageDTO generateMessage(String modelUri, List<MessageForGenerator> text, String context, double temperature) throws InterruptedException {
        String endPoint = "/generator/generate";

        RequestGenerateMessageDTO requestBody = new RequestGenerateMessageDTO();
        requestBody.setModel(modelUri);
        requestBody.setTemperature(temperature);
        requestBody.setContext(context);
        requestBody.setMessages(text);

        // 2) Оборачиваем его в HttpEntity с заголовками
        HttpEntity<RequestGenerateMessageDTO> request = new HttpEntity<>(requestBody, headers);

        // 3) Делаем POST и сразу получаем ваш ResponseGeneratedMessageDTO
        return restTemplate.postForObject(
                baseUrl + endPoint,
                request,
                ResponseGeneratedMessageDTO.class
        );
    }

    // Пока что будет без реализации векторов
    @Override
    public ResponseGeneratedVectorDTO generateVector(String modelUri, String text) {
        return null;
    }
}
