package com.promts.promts_test_server.promts_backend.service.Auth;

import com.promts.promts_test_server.promts_backend.dto.Shared.outbound.SuccessMessageDTO;
import com.promts.promts_test_server.promts_backend.dto.Auth.outbound.SuccessLoginDTO;

public interface AuthService {
    String getUidFirebaseAndValidate(String authorization);
    SuccessMessageDTO registerNewUser(String email, String password);
    SuccessMessageDTO actualiseToken(String authorization);
    SuccessLoginDTO loginUserGetJWTToken(String email, String password);

}
