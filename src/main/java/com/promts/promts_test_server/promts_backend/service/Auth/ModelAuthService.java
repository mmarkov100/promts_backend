package com.promts.promts_test_server.promts_backend.service.Auth;

import com.promts.promts_test_server.promts_backend.dto.Shared.outbound.SuccessMessageDTO;
import com.promts.promts_test_server.promts_backend.dto.Auth.outbound.SuccessLoginDTO;

public class ModelAuthService implements AuthService{
    @Override
    public String getUidFirebaseAndValidate(String authorization) {
        return "";
    }

    @Override
    public SuccessMessageDTO registerNewUser(String email, String password) {
        return null;
    }

    @Override
    public SuccessMessageDTO actualiseToken(String authorization) {
        return null;
    }

    @Override
    public SuccessLoginDTO loginUserGetJWTToken(String email, String password) {
        return null;
    }
}
