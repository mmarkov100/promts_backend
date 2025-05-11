package com.promts.promts_test_server.promts_backend.service.Auth;

import com.promts.promts_test_server.promts_backend.config.MockBackendConfig;
import com.promts.promts_test_server.promts_backend.dto.Shared.outbound.SuccessMessageDTO;
import com.promts.promts_test_server.promts_backend.dto.Auth.outbound.SuccessLoginDTO;
import com.promts.promts_test_server.shared.exception.GlobalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
@Profile({"neuro"})
public class MockAuthService implements AuthService{

    private static final Logger logger = Logger.getLogger(MockAuthService.class.getName());

    private final String mockJWTToken = "Bearer 1234jwt";

    private final MockBackendConfig mockBackendConfig;
    @Autowired
    public MockAuthService(MockBackendConfig mockBackendConfig) {
        this.mockBackendConfig = mockBackendConfig;
    }

    // Происходит проверка токена для его актуализации
    @Override
    public String getUidFirebaseAndValidate(String authorization){
        if (authorization.isEmpty()) {
            throw new GlobalException("TOKEN_ACTUALISE_ERROR", "Токен авторизации недействителен");
        } else if (!authorization.equals(mockJWTToken)) {
            throw new GlobalException("TOKEN_ACTUALISE_ERROR", "Токен авторизации недействителен");
        }

        // Выдает uidFirebase пользователя, но мокова пока что только это слово
        return mockBackendConfig.getUserUidFirebase();
    }

    @Override
    public SuccessMessageDTO registerNewUser(String email, String password){
        if (email.isEmpty() || password.isEmpty()){
            logger.info("Отсутствие логина или пароля");
            throw new GlobalException("REGISTRATION_ERROR", "Отсутствует логин или пароль");
        }

        SuccessMessageDTO response = new SuccessMessageDTO(true, "Успешно создан аккаунт (на самом деле не создан, это мок)");
        logger.info("Все круто: " + response);
        return response;
    }

    @Override
    public SuccessMessageDTO actualiseToken(String authorization) {

        if (authorization.isEmpty()){

            throw new GlobalException("TOKEN_ACTUALISE_ERROR", "Токен авторизации недействителен");
        } else if (!authorization.equals(mockJWTToken)) {
            throw new GlobalException("TOKEN_ACTUALISE_ERROR", "Токен авторизации недействителен");
        }
        return new SuccessMessageDTO(true, "Успешный вход в аккаунт");
    }

    @Override
    public SuccessLoginDTO loginUserGetJWTToken(String email, String password) {

        // Тут происходит проверка логина и пароля, моково пока что просто проверка на пустоту
        if (email.isEmpty() || password.isEmpty()){
            throw new GlobalException("LOGIN_ERROR", "Отсутствует логин или пароль");
        }

        String[] jwt = mockJWTToken.split(" ");
        return new SuccessLoginDTO(true, "Успешный вход в аккаунт", jwt[1]);
    }
}
