package com.promts.promts_test_server.repository.User;

import com.promts.promts_test_server.config.MockConfig;
import com.promts.promts_test_server.dto.Chat.outbound.ChatListShortDTO;
import com.promts.promts_test_server.dto.User.inbound.UpdateUserRequestDTO;
import com.promts.promts_test_server.dto.User.inbound.UserInfoFromModelDTO;
import com.promts.promts_test_server.dto.User.outbound.UserDTO;
import com.promts.promts_test_server.exception.GlobalException;
import com.promts.promts_test_server.repository.Chat.MockChatRepository;
import com.promts.promts_test_server.repository.NeuralNetwork.MockNeuralNetworkRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
@Profile("mock")
public class MockUserRepository implements UserRepository{

    @Autowired
    private MockNeuralNetworkRepository mockNeuralNetworkService;
    @Autowired
    private MockChatRepository mockChatRepository;
    @Autowired
    private MockConfig mockConfig;

    // В моковом вместо создания просто заранее созданный пустой аккаунт. Новый аккаунт в моковом режиме создать нельзя
    @Override
    public UserInfoFromModelDTO createUser(String email, String uidFirebase) throws InterruptedException {

        // Имитация ожидания запроса
        Thread.sleep(mockConfig.getDelay());

        try{

            return mockResponseUserWithChatsAndNeuros.getFirst();
        } catch (RuntimeException e) {
            throw new GlobalException("SERVICE_IS_NOT_ACTIVE", "Сервис недоступен");
        }
    }

    // При входе получаем всегда 2 аккаунт, заполненный уже информацией
    @Override
    public UserInfoFromModelDTO getUser(String uidFirebase) throws InterruptedException {

        // Имитация ожидания запроса
        Thread.sleep(mockConfig.getDelay());

        try{
            return mockResponseUserWithChatsAndNeuros.get(1);
        } catch (RuntimeException e) {
            throw new GlobalException("SERVICE_IS_NOT_ACTIVE", "Сервис недоступен");
        }
    }

    @Override
    public UserDTO newGetUser(String uidFirebase) throws InterruptedException {

        // Имитация ожидания запроса
        Thread.sleep(mockConfig.getDelay());

        try{
            return mockResponseUser.get(1);
        } catch (RuntimeException e) {
            throw new GlobalException("SERVICE_IS_NOT_ACTIVE", "Сервис недоступен");
        }
    }

    // Можем обновить настройки пользователей моковых
    @Override
    public UserInfoFromModelDTO updateUser(Long id, String uidFirebase, UpdateUserRequestDTO updateDTO) throws InterruptedException {

        // Имитация ожидания запроса
        Thread.sleep(mockConfig.getDelay());

        try{
            UserInfoFromModelDTO userInfoFromModelDTO = mockResponseUserWithChatsAndNeuros.get((int) (id-1));
            if (updateDTO.getMemory() != null) {
                userInfoFromModelDTO.getUser().setMemory(updateDTO.getMemory());
            }
            if (updateDTO.getStandardModelUriId() != null) {
                userInfoFromModelDTO.getUser().setStandardModelUriId(updateDTO.getStandardModelUriId());
            }
            if (updateDTO.isMemoryEnabled() != userInfoFromModelDTO.getUser().isMemoryEnabled()) {
                userInfoFromModelDTO.getUser().setMemoryEnabled(updateDTO.isMemoryEnabled());
            }
            if (updateDTO.isAiCanUpdateMemory() != userInfoFromModelDTO.getUser().isAiCanUpdateMemory()){
                userInfoFromModelDTO.getUser().setAiCanUpdateMemory(updateDTO.isAiCanUpdateMemory());
            }

            return userInfoFromModelDTO;
        } catch (RuntimeException e) {
            throw new GlobalException("SERVICE_IS_NOT_ACTIVE", "Сервис недоступен");
        }
    }

    @Override
    public UserDTO newUpdateUser(Long id, String uidFirebase, UpdateUserRequestDTO updateDTO) throws InterruptedException {

        // Имитация ожидания запроса
        Thread.sleep(mockConfig.getDelay());

        try{
            UserDTO userDTO = mockResponseUser.get((int) (id-1));
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

    public final List<UserDTO> mockResponseUser = new ArrayList<>();

    @PostConstruct
    private void initMockResponseUser() {

        // Мок предоставляется при регистрации нового пользователя
        mockResponseUser.add(new UserDTO(
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
        mockResponseUser.add(new UserDTO(
                2L,
                "he@he.he",
                "334cxlllasd33",
                "USER",
                2L,
                20.00,
                "Пользователь просит обращаться к нему мой господин",
                true,
                false,
                LocalDateTime.parse("2023-10-01T12:00:10")
        ));
    }

    @PostConstruct
    private void initMockResponseUserWithChatsAndNeuros() throws InterruptedException {
        // Моковые данные нейросетей, у всех пользователей они одинаковые
        List<UserInfoFromModelDTO.NeuralNetwork> neuralNetworks = new ArrayList<>();
        for (int i = 0; i < mockNeuralNetworkService.mockNeuralNetworkDTOS.size(); i++) {
            neuralNetworks.add(new UserInfoFromModelDTO.NeuralNetwork(
                    mockNeuralNetworkService.mockNeuralNetworkDTOS.get(i).getId(),
                    mockNeuralNetworkService.mockNeuralNetworkDTOS.get(i).getName(),
                    mockNeuralNetworkService.mockNeuralNetworkDTOS.get(i).getSystemName(),
                    mockNeuralNetworkService.mockNeuralNetworkDTOS.get(i).getDesc()));
        }

        // Мок предоставляется при регистрации нового пользователя
        mockResponseUserWithChatsAndNeuros.add(new UserInfoFromModelDTO(
                new UserInfoFromModelDTO.User(
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
                ),
                new ArrayList<>(),
                neuralNetworks
        ));


        // Мок предоставляется при входе существующего пользователя, идет вместе с чатами
        List<UserInfoFromModelDTO.ChatList> chats2 = new ArrayList<>();
        ChatListShortDTO chatListShortDTO =  mockChatRepository.getUserChats("3233", 2L);
        for (int i = 0; i < chatListShortDTO.getChats().size(); i++){
            chats2.add(new UserInfoFromModelDTO.ChatList(
                    chatListShortDTO.getChats().get(i).getId(),
                    chatListShortDTO.getChats().get(i).getChatName(),
                    chatListShortDTO.getChats().get(i).isStarredChat(),
                    chatListShortDTO.getChats().get(i).getDateEdit()
            ));
        }
        mockResponseUserWithChatsAndNeuros.add(new UserInfoFromModelDTO(
                new UserInfoFromModelDTO.User(
                        2L,
                        "he@he.he",
                        "334cxlllasd33",
                        "USER",
                        2L,
                        20.00,
                        "Пользователь просит обращаться к нему мой господин",
                        true,
                        false,
                        LocalDateTime.parse("2023-10-01T12:00:10")
                ),
                chats2,
                neuralNetworks
        ));
    }
}
