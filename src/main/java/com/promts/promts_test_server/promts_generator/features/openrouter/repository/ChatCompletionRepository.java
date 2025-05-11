package com.promts.promts_test_server.promts_generator.features.openrouter.repository;

import com.promts.promts_test_server.promts_generator.features.openrouter.config.OpenRouterConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
public class ChatCompletionRepository {

    private static final Logger logger = LoggerFactory.getLogger(ChatCompletionRepository.class);

    private final RestTemplate restTemplate;
    private final HttpHeaders headers;
    private final String baseUrl;

    @Autowired
    public ChatCompletionRepository(RestTemplate restTemplate,
                                    OpenRouterConfig config) {
        this.restTemplate = restTemplate;
        this.baseUrl = config.getUrlOpenRouterGeneration();
        this.headers = new HttpHeaders();
        headers.setBearerAuth(config.getApiKeyOpenRouter());
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    public <Req, Res> Res post(Req requestBody, Class<Res> responseType) {
        HttpEntity<Req> request = new HttpEntity<>(requestBody, headers);
        logger.info(request.toString());
        return restTemplate.postForObject(baseUrl, request, responseType);
    }
}
