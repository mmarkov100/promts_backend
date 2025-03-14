package com.promts.promts_test_server.service.Auth;

import com.promts.promts_test_server.dto.Auth.outbound.SuccessAuthDTO;
import com.promts.promts_test_server.dto.Auth.outbound.SuccessLoginDTO;

public class ModelAuthService implements AuthService{
    @Override
    public String getUidFirebaseAndValidate(String authorization) {
        return "";
    }

    @Override
    public SuccessAuthDTO registerNewUser(String email, String password) {

        return null;
    }

    @Override
    public SuccessAuthDTO actualiseToken(String authorization) {
        return null;
    }

    @Override
    public SuccessLoginDTO loginUserGetJWTToken(String email, String password) {

        return null;
    }
}
