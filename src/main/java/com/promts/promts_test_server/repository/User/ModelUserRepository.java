package com.promts.promts_test_server.repository.User;

import com.promts.promts_test_server.dto.User.inbound.UpdateUserRequestDTO;
import com.promts.promts_test_server.dto.User.inbound.UserModelDTO;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile("model")
public class ModelUserRepository implements UserRepository{

    @Override
    public UserModelDTO newGetUser(String uidFirebase) throws InterruptedException {
        return null;
    }

    @Override
    public UserModelDTO newUpdateUser(String uidFirebase, UpdateUserRequestDTO updateDTO) throws InterruptedException {
        return null;
    }
}
