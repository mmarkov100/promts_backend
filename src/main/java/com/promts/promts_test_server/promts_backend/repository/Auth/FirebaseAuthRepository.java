package com.promts.promts_test_server.promts_backend.repository.Auth;

import com.google.firebase.auth.*;
import com.promts.promts_test_server.promts_backend.dto.Auth.inboind.FirebaseSignInResponse;
import com.promts.promts_test_server.promts_backend.dto.User.inbound.UserModelDTO;
import com.promts.promts_test_server.shared.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Repository
@Profile("firebase")
@RequiredArgsConstructor
public class FirebaseAuthRepository implements AuthRepository {

    private final FirebaseAuth firebaseAuth;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${firebase.api-key}")
    private String apiKey;

    /** Создание учётной записи в Firebase */
    @Override
    public UserModelDTO registerUser(String email, String password) {
        try {
            UserRecord record = firebaseAuth.createUser(
                    new UserRecord.CreateRequest()
                            .setEmail(email)
                            .setPassword(password)      // ← реальный пароль
                            .setEmailVerified(false)
            );

            UserModelDTO dto = new UserModelDTO();
            dto.setEmail(email);
            dto.setUidFirebase(record.getUid());
            return dto;

        } catch (FirebaseAuthException e) {
            // пример разбора кодов — пригодится для фронта
            if ("EMAIL_ALREADY_EXISTS".equals(e.getAuthErrorCode().name())) {
                throw new GlobalException("USER_EXISTS", "Такой e-mail уже зарегистрирован");
            }
            throw new GlobalException("REGISTRATION_ERROR", e.getMessage());
        }
    }

    /** Вход. Firebase вернёт idToken */
    public String signInAndGetToken(String email, String password) {

        String url = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=" + apiKey;

        /* тело запроса в виде Map – RestTemplate сам превратит его в JSON */
        Map<String, Object> body = Map.of(
                "email", email,
                "password", password,
                "returnSecureToken", true
        );

        HttpHeaders h = new HttpHeaders();
        h.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<FirebaseSignInResponse> resp = restTemplate.exchange(
                url,
                HttpMethod.POST,
                new HttpEntity<>(body, h),
                FirebaseSignInResponse.class            // ← маппинг Jackson
        );

        if (resp.getStatusCode().is2xxSuccessful() && resp.getBody() != null) {
            return resp.getBody().getIdToken();
        }
        throw new GlobalException("LOGIN_ERROR", "Не удалось войти");
    }

    /** Проверяем Bearer-токен и возвращаем uidFirebase */
    public String verify(String bearer) {
        try {
            String token = bearer.split(" ")[1];
            FirebaseToken decoded = firebaseAuth.verifyIdToken(token);
            return decoded.getUid();
        } catch (Exception e) {
            throw new GlobalException("TOKEN_VALIDATION_ERROR", "Неверный токен");
        }
    }
}
