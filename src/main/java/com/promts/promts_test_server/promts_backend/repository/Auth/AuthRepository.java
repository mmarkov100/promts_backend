package com.promts.promts_test_server.promts_backend.repository.Auth;

import com.promts.promts_test_server.promts_backend.dto.User.inbound.UserModelDTO;

public interface AuthRepository {
    UserModelDTO registerUser(String email, String password);
}
