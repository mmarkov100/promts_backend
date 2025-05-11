package com.promts.promts_test_server.promts_backend.repository.User;

import com.promts.promts_test_server.promts_backend.config.MockBackendConfig;
import com.promts.promts_test_server.promts_backend.dto.User.inbound.UpdateUserRequestDTO;
import com.promts.promts_test_server.promts_backend.dto.User.inbound.UserModelDTO;
import com.promts.promts_test_server.shared.exception.GlobalException;
import com.promts.promts_test_server.promts_backend.repository.NeuralNetwork.NeuralNetworkRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@Profile({"mock", "neuro"})
public class MockUserRepository implements UserRepository{

    @Autowired
    private NeuralNetworkRepository networkRepository;
    @Autowired
    private MockBackendConfig mockBackendConfig;

    @Override
    public UserModelDTO getUser(String uidFirebase) throws InterruptedException {

        // Имитация ожидания запроса
        Thread.sleep(mockBackendConfig.getDelay());

        try{
            return getUserModelDTO(uidFirebase);
        } catch (RuntimeException e) {
            throw new GlobalException("SERVICE_IS_NOT_ACTIVE", "Сервис недоступен");
        }
    }

    @Override
    public UserModelDTO updateUser(String uidFirebase, UpdateUserRequestDTO updateDTO) throws InterruptedException {

        // Имитация ожидания запроса
        Thread.sleep(mockBackendConfig.getDelay());

        try{
            UserModelDTO userDTO =getUserModelDTO(uidFirebase);
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

    private UserModelDTO getUserModelDTO(String uidFirebase) {
        Optional<UserModelDTO> userOpt = mockResponseUser.stream().filter(user -> Objects.equals(user.getUidFirebase(), uidFirebase)).findFirst();
        if (userOpt.isEmpty()) {
            throw new GlobalException("USER_NOT_FOUND", "Пользователь не найден");
        }
        return userOpt.get();
    }

    public final List<UserModelDTO> mockResponseUser = new ArrayList<>();

    @PostConstruct
    private void initMockResponseUser() {

        // Мок предоставляется при регистрации нового пользователя
        mockResponseUser.add(new UserModelDTO(
                0L,
                "user@example.com",
                "44asds23l0332a",
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
                1L,
                "he@he.he",
                "334cxlllasd33",
                "USER",
                0L,
                20.00,
                "Пользователь просит обращаться к нему мой господин",
                true,
                true,
                LocalDateTime.parse("2023-10-01T12:00:10")
        ));
    }
}
