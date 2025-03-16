package com.promts.promts_test_server.repository.User;

import com.promts.promts_test_server.dto.User.inbound.UpdateUserRequestDTO;
import com.promts.promts_test_server.dto.User.inbound.UserModelDTO;

public interface UserRepository {
    UserModelDTO newGetUser(String uidFirebase) throws InterruptedException;
    UserModelDTO newUpdateUser(Long id, String uidFirebase, UpdateUserRequestDTO updateDTO) throws InterruptedException;
}
