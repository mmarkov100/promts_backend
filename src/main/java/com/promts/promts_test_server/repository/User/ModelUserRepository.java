package com.promts.promts_test_server.repository.User;

import com.promts.promts_test_server.dto.User.inbound.UpdateUserRequestDTO;
import com.promts.promts_test_server.dto.User.inbound.UserInfoFromModelDTO;
import com.promts.promts_test_server.dto.User.outbound.UserDTO;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile("model")
public class ModelUserRepository implements UserRepository{
    @Override
    public UserInfoFromModelDTO createUser(String email, String uidFirebase) {
        return null;
    }

    @Override
    public UserInfoFromModelDTO getUser(String uidFirebase) {
        return null;
    }

    @Override
    public UserDTO newGetUser(String uidFirebase) throws InterruptedException {
        return null;
    }

    @Override
    public UserInfoFromModelDTO updateUser(Long id, String uidFirebase, UpdateUserRequestDTO updateDTO) {
        return null;
    }

    @Override
    public UserDTO newUpdateUser(Long id, String uidFirebase, UpdateUserRequestDTO updateDTO) throws InterruptedException {
        return null;
    }
}
