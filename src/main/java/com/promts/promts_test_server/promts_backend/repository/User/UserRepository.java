package com.promts.promts_test_server.promts_backend.repository.User;

import com.promts.promts_test_server.promts_backend.dto.User.inbound.UpdateUserRequestDTO;
import com.promts.promts_test_server.promts_backend.dto.User.inbound.UserModelDTO;

public interface UserRepository {
    UserModelDTO getUser(String uidFirebase) throws InterruptedException;
    UserModelDTO updateUser(String uidFirebase, UpdateUserRequestDTO updateDTO) throws InterruptedException;
}
