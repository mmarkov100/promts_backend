package com.promts.promts_test_server.service.Auth;

import com.promts.promts_test_server.dto.Auth.outbound.SuccessAuthDTO;
import com.promts.promts_test_server.dto.Auth.outbound.SuccessLoginDTO;
import com.promts.promts_test_server.exception.GlobalException;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("mock")
public class MockAuthService implements AuthService{

    private final String mockJWTToken = "1234jwt";

    // Происходит проверка токена для его актуализации
    @Override
    public String getUidFirebaseAndValidate(String authorization){
        if (authorization.isEmpty()) {
            throw new GlobalException("TOKEN_ACTUALISE_ERROR", "Токен авторизации недействителен");
        } else if (!authorization.equals(mockJWTToken)) {
            throw new GlobalException("TOKEN_ACTUALISE_ERROR", "Токен авторизации недействителен");
        }
        return "approved";
    }

    @Override
    public SuccessAuthDTO registerNewUser(String email, String password){
        if (email.isEmpty() || password.isEmpty()){
            throw new GlobalException("REGISTRATION_ERROR", "Отсутствует логин или пароль");
        }
        return new SuccessAuthDTO(true, "Успешно создан аккаунт (на самом деле не создан, это мок)");
    }

    @Override
    public SuccessAuthDTO actualiseToken(String authorization) {
        if (authorization.isEmpty()){
            throw new GlobalException("TOKEN_ACTUALISE_ERROR", "Токен авторизации недействителен");
        } else if (!authorization.equals(mockJWTToken)) {
            throw new GlobalException("TOKEN_ACTUALISE_ERROR", "Токен авторизации недействителен");
        }
        return new SuccessAuthDTO(true, "Успешный вход в аккаунт");
    }

    @Override
    public SuccessLoginDTO loginUserGetJWTToken(String email, String password) {
        if (email.isEmpty() || password.isEmpty()){
            throw new GlobalException("LOGIN_ERROR", "Отсутствует логин или пароль");
        }
        return new SuccessLoginDTO(true, "Успешный вход в аккаунт", mockJWTToken);
    }
}
