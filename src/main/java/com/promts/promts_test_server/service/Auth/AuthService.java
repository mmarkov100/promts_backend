package com.promts.promts_test_server.service.Auth;

import com.promts.promts_test_server.dto.Auth.outbound.SuccessAuthDTO;
import com.promts.promts_test_server.dto.Auth.outbound.SuccessLoginDTO;

public interface AuthService {
    String getUidFirebaseAndValidate(String authorization);
    SuccessAuthDTO registerNewUser(String email, String password);
    SuccessAuthDTO actualiseToken(String authorization);
    SuccessLoginDTO loginUserGetJWTToken(String email, String password);

}
