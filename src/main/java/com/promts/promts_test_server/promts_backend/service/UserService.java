package com.promts.promts_test_server.promts_backend.service;

import com.promts.promts_test_server.promts_backend.dto.User.inbound.UpdateUserRequestDTO;
import com.promts.promts_test_server.promts_backend.dto.User.inbound.UserModelDTO;
import com.promts.promts_test_server.promts_backend.dto.User.outbound.UserDTO;
import com.promts.promts_test_server.promts_backend.repository.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserDTO newGetUser(String uidFirebase) throws InterruptedException {

        UserModelDTO userModelDTO = userRepository.newGetUser(uidFirebase);

        return new UserDTO(
                userModelDTO.getId(),
                userModelDTO.getEmail(),
                userModelDTO.getRole(),
                userModelDTO.getMoney(),
                userModelDTO.getMemory(),
                userModelDTO.isMemoryEnabled(),
                userModelDTO.isAiCanUpdateMemory(),
                userModelDTO.getStandardModelUriId()
        );
    }

    public UserDTO newUpdateUser(String uidFirebase,
                                      UpdateUserRequestDTO requestDTO) throws InterruptedException {

        UserModelDTO userModelDTO = userRepository.newUpdateUser(uidFirebase, requestDTO);

        return new UserDTO(
                userModelDTO.getId(),
                userModelDTO.getEmail(),
                userModelDTO.getRole(),
                userModelDTO.getMoney(),
                userModelDTO.getMemory(),
                userModelDTO.isMemoryEnabled(),
                userModelDTO.isAiCanUpdateMemory(),
                userModelDTO.getStandardModelUriId()
        );
    }
}
