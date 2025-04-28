package com.promts.promts_test_server.repository.User;

import com.promts.promts_test_server.config.MockConfig;
import com.promts.promts_test_server.controller.NeuralNetworkController;
import com.promts.promts_test_server.dto.User.inbound.UpdateUserRequestDTO;
import com.promts.promts_test_server.dto.User.inbound.UserInfoFromModelDTO;
import com.promts.promts_test_server.dto.User.inbound.UserModelDTO;
import com.promts.promts_test_server.exception.GlobalException;
import com.promts.promts_test_server.repository.NeuralNetwork.MockNeuralNetworkRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Repository
@Profile("mock")
public class MockUserRepository implements UserRepository{

    private static final Logger logger = Logger.getLogger(MockUserRepository.class.getName());

    @Autowired
    private MockNeuralNetworkRepository mockNeuralNetworkService;
    @Autowired
    private MockConfig mockConfig;

    @Override
    public UserModelDTO newGetUser(String uidFirebase) throws InterruptedException {

        // Имитация ожидания запроса
        Thread.sleep(mockConfig.getDelay());

        try{
            UserModelDTO output =  mockResponseUser.get(1);
            logger.info("USER: " + output.toString());
            return mockResponseUser.get(1);
        } catch (RuntimeException e) {
            throw new GlobalException("SERVICE_IS_NOT_ACTIVE", "Сервис недоступен");
        }
    }

    @Override
    public UserModelDTO newUpdateUser(String uidFirebase, UpdateUserRequestDTO updateDTO) throws InterruptedException {

        // Имитация ожидания запроса
        Thread.sleep(mockConfig.getDelay());

        try{
            UserModelDTO userDTO = mockResponseUser.get((int) (1));
            if (updateDTO.getMemory() != null) {
                userDTO.setMemory(updateDTO.getMemory());
            }
            if (updateDTO.getStandardModelUriId() != null) {
                userDTO.setStandardModelUriId(updateDTO.getStandardModelUriId());
            }
            if (updateDTO.isMemoryEnabled() != userDTO.isMemoryEnabled()) {
                userDTO.setMemoryEnabled(updateDTO.isMemoryEnabled());
            }
            if (updateDTO.isAiCanUpdateMemory() != userDTO.isAiCanUpdateMemory()){
                userDTO.setAiCanUpdateMemory(updateDTO.isAiCanUpdateMemory());
            }

            return userDTO;
        } catch (RuntimeException e) {
            throw new GlobalException("SERVICE_IS_NOT_ACTIVE", "Сервис недоступен");
        }
    }

    public final List<UserInfoFromModelDTO> mockResponseUserWithChatsAndNeuros = new ArrayList<>();

    public final List<UserModelDTO> mockResponseUser = new ArrayList<>();

    @PostConstruct
    private void initMockResponseUser() {

        // Мок предоставляется при регистрации нового пользователя
        mockResponseUser.add(new UserModelDTO(
                1L,
                "user@example.com",
                "44asds23l0332a;",
                "USER",
                1L,
                20.00,
                "",
                true,
                true,
                LocalDateTime.parse("2023-10-01T12:00:10")
        ));
        // Мок предоставляется при входе существующего пользователя, идет вместе с чатами
        mockResponseUser.add(new UserModelDTO(
                2L,
                "he@he.he",
                "334cxlllasd33",
                "USER",
                3L,
                20.00,
                "Пользователь просит обращаться к нему мой господин",
                true,
                false,
                LocalDateTime.parse("2023-10-01T12:00:10")
        ));
    }
}
