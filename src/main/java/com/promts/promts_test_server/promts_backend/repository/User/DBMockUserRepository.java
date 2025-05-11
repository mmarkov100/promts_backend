package com.promts.promts_test_server.promts_backend.repository.User;

import com.promts.promts_test_server.promts_backend.config.MockBackendConfig;
import com.promts.promts_test_server.promts_backend.dto.User.inbound.UpdateUserRequestDTO;
import com.promts.promts_test_server.promts_backend.dto.User.inbound.UserModelDTO;
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

@Repository
@Profile({"dbmock"})
public class DBMockUserRepository implements  UserRepository{

    private final RestTemplate restTemplate;
    private final HttpHeaders headers;
    private final String baseUrl;

    @Autowired
    public DBMockUserRepository(RestTemplate restTemplate, MockBackendConfig mockBackendConfig) {
        this.restTemplate = restTemplate;
        this.headers = new HttpHeaders();
        this.baseUrl = mockBackendConfig.getUrlDatabase() +  "/db/users";
    }

    @Override
    public UserModelDTO getUser(String uidFirebase) {
        String endPoint = baseUrl + "/uid/" + uidFirebase;

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<UserModelDTO> response = restTemplate.exchange(
                    endPoint,
                    HttpMethod.GET,
                    entity,
                    UserModelDTO.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody();
            }
        } catch (HttpClientErrorException.NotFound e) {
            // Если пользователя нет, возвращаем пустоту
            throw new GlobalException("FAIL_FETCH_USER","Не удалось загрузить пользователя");
        }
        throw new GlobalException("FAIL_FETCH_USER","Не удалось загрузить пользователя");
    }

    @Override
    public UserModelDTO updateUser(String uidFirebase, UpdateUserRequestDTO updateDTO){
        String endPoint = baseUrl + "/uid/" + uidFirebase;

        HttpEntity<UpdateUserRequestDTO> entity = new HttpEntity<>(updateDTO, headers);

        try {
            ResponseEntity<UserModelDTO> response = restTemplate.exchange(
                    endPoint,
                    HttpMethod.PUT,
                    entity,
                    UserModelDTO.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody();
            }
        } catch (HttpClientErrorException.NotFound e) {
            // Если пользователя нет, возвращаем пустоту
            throw new GlobalException("FAIL_FETCH_USER","Не удалось обновить пользователя");
        }
        throw new GlobalException("FAIL_FETCH_USER","Не удалось обновить пользователя");
    }
}
