package com.promts.promts_test_server.promts_backend.repository.NeuralNetwork;

import com.promts.promts_test_server.promts_backend.config.MockBackendConfig;
import com.promts.promts_test_server.promts_backend.dto.NeuralNetwork.outbound.NeuralNetworkDTO;
import com.promts.promts_test_server.shared.exception.GlobalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Repository
@Profile({"dbmock"})
public class DBMockNeuralNetworkRepository implements NeuralNetworkRepository{

    private final RestTemplate restTemplate;
    private final HttpHeaders headers;
    private final String baseUrl;

    @Autowired
    public DBMockNeuralNetworkRepository(RestTemplate restTemplate, MockBackendConfig mockBackendConfig) {
        this.restTemplate = restTemplate;
        this.headers = new HttpHeaders();
        this.baseUrl = mockBackendConfig.getUrlDatabase()  + "/db/models";
    }

    @Override
    public List<NeuralNetworkDTO> getAllNeuros(String uidFirebase) {
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<NeuralNetworkDTO[]> response = restTemplate.exchange(
                    baseUrl,
                    HttpMethod.GET,
                    entity,
                    NeuralNetworkDTO[].class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return List.of(response.getBody());
            }
        } catch (HttpClientErrorException.NotFound e) {
            // Если нейросетей нет, возвращаем пустой список
            return List.of();
        }
        throw new GlobalException("FAIL_FETCH_NEUROS","Failed to get neuros");
    }
}
