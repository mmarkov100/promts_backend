package com.promts.promts_test_server.promts_backend.repository.Auth;

import com.promts.promts_test_server.promts_backend.config.MockBackendConfig;
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
public class DBMockAuthRepository implements AuthRepository{

    private final RestTemplate restTemplate;
    private final HttpHeaders headers;
    private final String baseUrl;

    @Autowired
    public DBMockAuthRepository(RestTemplate restTemplate, MockBackendConfig mockBackendConfig) {
        this.restTemplate = restTemplate;
        this.headers = new HttpHeaders();
        this.baseUrl = mockBackendConfig.getUrlDatabase() + "/db/users";
    }

    @Override
    public UserModelDTO registerUser(String email, String password) {
        UserModelDTO newUser = new UserModelDTO();
        newUser.setEmail(email);
        newUser.setUidFirebase(email);

        HttpEntity<UserModelDTO> entity = new HttpEntity<>(newUser, headers);

        try {
            ResponseEntity<UserModelDTO> response = restTemplate.exchange(
                    baseUrl,
                    HttpMethod.POST,
                    entity,
                    UserModelDTO.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody();
            }
        } catch (HttpClientErrorException.NotFound e) {
            // Если пользователя нет, возвращаем пустоту
            throw new GlobalException("FAIL_FETCH_USER","Не удалось зарегистрировать пользователя");
        }
        throw new GlobalException("FAIL_FETCH_USER","Не удалось зарегистрировать пользователя");
    }
}
