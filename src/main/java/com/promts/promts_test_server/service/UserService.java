package com.promts.promts_test_server.service;

import com.promts.promts_test_server.dto.User.inbound.UpdateUserRequestDTO;
import com.promts.promts_test_server.dto.User.inbound.UserInfoFromModelDTO;
import com.promts.promts_test_server.dto.User.outbound.GetUserResponseDTO;
import com.promts.promts_test_server.dto.User.outbound.UpdateUserResponseDTO;
import com.promts.promts_test_server.dto.User.outbound.UserDTO;
import com.promts.promts_test_server.exception.GlobalException;
import com.promts.promts_test_server.repository.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Сервис для создания пользователя, тестовый
    public GetUserResponseDTO createUser(String email, String uidFirebase) throws RuntimeException {
        try{
            UserInfoFromModelDTO userModelDto = userRepository.createUser(email, uidFirebase);

            List<GetUserResponseDTO.ChatList> chatLists  = new ArrayList<>();
            for (int i = 0; i < userModelDto.getChats().size(); i++){
                chatLists.add(new GetUserResponseDTO.ChatList(
                        userModelDto.getChats().get(i).getId(),
                        userModelDto.getChats().get(i).getChatName(),
                        userModelDto.getChats().get(i).isStarredChat(),
                        userModelDto.getChats().get(i).getDateEdit()
                ));
            }

            List<GetUserResponseDTO.NeuralNetwork> neuralNetworks = new ArrayList<>();
            for (int i = 0; i < userModelDto.getNeuralNetworks().size(); i++) {
                neuralNetworks.add(new GetUserResponseDTO.NeuralNetwork(
                        userModelDto.getNeuralNetworks().get(i).getId(),
                        userModelDto.getNeuralNetworks().get(i).getName(),
                        userModelDto.getNeuralNetworks().get(i).getSystemName(),
                        userModelDto.getNeuralNetworks().get(i).getDesc()
                ));
            }
            return new GetUserResponseDTO(
                    new GetUserResponseDTO.User(
                            userModelDto.getUser().getId(),
                            userModelDto.getUser().getEmail(),
                            userModelDto.getUser().getRole(),
                            userModelDto.getUser().getMoney(),
                            userModelDto.getUser().getMemory(),
                            userModelDto.getUser().isMemoryEnabled(),
                            userModelDto.getUser().isAiCanUpdateMemory(),
                            userModelDto.getUser().getStandardModelUriId()
                    ),
                    chatLists,
                    neuralNetworks
            );
        } catch (Exception e){
            throw new GlobalException("SERVICE_IS_NOT_ACTIVE", "Сервис недоступен");
        }
    }


    // Сервис для получения пользователя, тестовый
    public GetUserResponseDTO getUser(String uidFirebase) throws RuntimeException {
        try{
            UserInfoFromModelDTO userModelDto = userRepository.getUser(uidFirebase);

            List<GetUserResponseDTO.ChatList> chatLists  = new ArrayList<>();
            for (int i = 0; i < userModelDto.getChats().size(); i++){
                chatLists.add(new GetUserResponseDTO.ChatList(
                        userModelDto.getChats().get(i).getId(),
                        userModelDto.getChats().get(i).getChatName(),
                        userModelDto.getChats().get(i).isStarredChat(),
                        userModelDto.getChats().get(i).getDateEdit()
                ));
            }

            List<GetUserResponseDTO.NeuralNetwork> neuralNetworks = new ArrayList<>();
            for (int i = 0; i < userModelDto.getNeuralNetworks().size(); i++) {
                neuralNetworks.add(new GetUserResponseDTO.NeuralNetwork(
                        userModelDto.getNeuralNetworks().get(i).getId(),
                        userModelDto.getNeuralNetworks().get(i).getName(),
                        userModelDto.getNeuralNetworks().get(i).getSystemName(),
                        userModelDto.getNeuralNetworks().get(i).getDesc()
                ));
            }
            return new GetUserResponseDTO(
                    new GetUserResponseDTO.User(
                            userModelDto.getUser().getId(),
                            userModelDto.getUser().getEmail(),
                            userModelDto.getUser().getRole(),
                            userModelDto.getUser().getMoney(),
                            userModelDto.getUser().getMemory(),
                            userModelDto.getUser().isMemoryEnabled(),
                            userModelDto.getUser().isAiCanUpdateMemory(),
                            userModelDto.getUser().getStandardModelUriId()
                    ),
                    chatLists,
                    neuralNetworks
            );
        } catch (Exception e){
            throw new GlobalException("SERVICE_IS_NOT_ACTIVE", "Сервис недоступен");
        }
    }

    public UserDTO newGetUser(String uidFirebase) throws RuntimeException, InterruptedException {

        return userRepository.newGetUser(uidFirebase);
    }

    // Метод для обновления настроек пользователя
    public UpdateUserResponseDTO updateUser(String uidFirebase,
                                            Long userId,
                                            UpdateUserRequestDTO requestDTO) {
        try{
            UserInfoFromModelDTO userModelDto = userRepository.updateUser(userId, uidFirebase, requestDTO);

            // Заполняем обновленные данные
            UpdateUserResponseDTO updateUser = new UpdateUserResponseDTO();
            updateUser.setUpdated(true);
            updateUser.setMemoryEnabled(userModelDto.getUser().isMemoryEnabled());
            updateUser.setAiCanUpdateMemory(userModelDto.getUser().isAiCanUpdateMemory());
            updateUser.setMemory(userModelDto.getUser().getMemory());
            updateUser.setStandardModelUriId(userModelDto.getUser().getStandardModelUriId());

            return updateUser;
        } catch (Exception e){
            throw new GlobalException("SERVICE_IS_NOT_ACTIVE", "Сервис недоступен");
        }
    }

    public UserDTO newUpdateUser(String uidFirebase,
                                 Long userId,
                                 UpdateUserRequestDTO requestDTO) throws InterruptedException {

        return userRepository.newUpdateUser(userId, uidFirebase, requestDTO);
    }
}
