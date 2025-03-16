package com.promts.promts_test_server.repository.User;

import com.promts.promts_test_server.dto.User.inbound.UpdateUserRequestDTO;
import com.promts.promts_test_server.dto.User.inbound.UserInfoFromModelDTO;
import com.promts.promts_test_server.dto.User.outbound.UserDTO;

public interface UserRepository {
    UserDTO newGetUser(String uidFirebase) throws InterruptedException;
    UserDTO newUpdateUser(Long id, String uidFirebase, UpdateUserRequestDTO updateDTO) throws InterruptedException;
}
