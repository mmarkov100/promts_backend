package com.promts.promts_test_server.repository.User;

import com.promts.promts_test_server.dto.User.inbound.UpdateUserRequestDTO;
import com.promts.promts_test_server.dto.User.inbound.UserInfoFromModelDTO;
import com.promts.promts_test_server.dto.User.outbound.UserDTO;

public interface UserRepository {
    UserInfoFromModelDTO createUser(String email, String uidFirebase) throws InterruptedException;
    UserInfoFromModelDTO getUser(String uidFirebase) throws InterruptedException;
    UserDTO newGetUser(String uidFirebase) throws InterruptedException;
    UserInfoFromModelDTO updateUser(Long id, String uidFirebase, UpdateUserRequestDTO updateDTO) throws InterruptedException;
    UserDTO newUpdateUser(Long id, String uidFirebase, UpdateUserRequestDTO updateDTO) throws InterruptedException;
}
