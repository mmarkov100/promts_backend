package com.promts.promts_test_server.promts_generator.features.yandexgpt.repository;

import com.promts.promts_test_server.promts_generator.features.yandexgpt.config.YandexConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
public class ChatYandexCompletionRepository {

    private static final Logger logger = LoggerFactory.getLogger(ChatYandexCompletionRepository.class);

    private final RestTemplate restTemplate;
    private final HttpHeaders headers;
    private final String baseUrl;

    @Autowired
    public ChatYandexCompletionRepository(RestTemplate restTemplate,
                                          YandexConfig config) {
        this.restTemplate = restTemplate;
        this.baseUrl = "https://llm.api.cloud.yandex.net/foundationModels/v1/completion";
        this.headers = new HttpHeaders();
        headers.set("Authorization", "Api-Key " + config.getApiKey());
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    public <Req, Res> Res post(Req requestBody, Class<Res> responseType) {
        HttpEntity<Req> request = new HttpEntity<>(requestBody, headers);
        logger.info(request.toString());
        return restTemplate.postForObject(baseUrl, request, responseType);
    }
}