package com.promts.promts_test_server.promts_backend.service.Message;

import com.promts.promts_test_server.promts_backend.config.MockBackendConfig;
import com.promts.promts_test_server.promts_backend.dto.Chat.inbound.ChatModelDTO;
import com.promts.promts_test_server.promts_backend.dto.Generator.inbound.ResponseGeneratedMessageDTO;
import com.promts.promts_test_server.promts_backend.dto.Generator.outbound.MessageForGenerator;
import com.promts.promts_test_server.promts_backend.dto.Message.inbound.DeleteMessagesDTO;
import com.promts.promts_test_server.promts_backend.dto.Message.inbound.MessageModelDTO;
import com.promts.promts_test_server.promts_backend.dto.Message.inbound.NewMessageDTO;
import com.promts.promts_test_server.promts_backend.dto.Message.inbound.RegenerateMessageDTO;
import com.promts.promts_test_server.promts_backend.dto.Message.outbound.MessageGenerateResponse;
import com.promts.promts_test_server.promts_backend.dto.Message.outbound.SaveMessageDTO;
import com.promts.promts_test_server.promts_backend.dto.Message.outbound.SuccessDeleteMessagesDTO;
import com.promts.promts_test_server.promts_backend.dto.NeuralNetwork.outbound.NeuralNetworkDTO;
import com.promts.promts_test_server.promts_backend.dto.User.inbound.UserModelDTO;
import com.promts.promts_test_server.shared.exception.GlobalException;
import com.promts.promts_test_server.promts_backend.repository.Chat.MockChatRepository;
import com.promts.promts_test_server.promts_backend.repository.DataSore.MockDataStore;
import com.promts.promts_test_server.promts_backend.repository.Generator.GeneratorRepository;
import com.promts.promts_test_server.promts_backend.repository.Message.MessageRepository;
import com.promts.promts_test_server.promts_backend.repository.NeuralNetwork.MockNeuralNetworkRepository;
import com.promts.promts_test_server.promts_backend.repository.User.MockUserRepository;
import com.promts.promts_test_server.promts_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

@Service
@Profile({"mock"})
public class MockMessageService implements MessageService{

    private static final Logger logger = Logger.getLogger(MockMessageService.class.getName());

    private final MockBackendConfig mockBackendConfig;
    private final MockDataStore mockDataStore;
    @Autowired
    public MockMessageService(MockBackendConfig mockBackendConfig, MockDataStore mockDataStore) {
        this.mockBackendConfig = mockBackendConfig;
        this.mockDataStore = mockDataStore;
    }

    @Override
    public MessageGenerateResponse generateMessage(String uidFirebase, NewMessageDTO newMessageDTO) throws InterruptedException {

        // Имитация ожидания запроса
        Thread.sleep(mockBackendConfig.getDelay());

        // Определяем, существует ли такая нейросеть в бд
        Optional<NeuralNetworkDTO> neuralNetworkOpt = mockNeuralNetworkRepository.mockNeuralNetworkDTOS.stream()
                .filter(neuro -> Objects.equals(neuro.getId(), newMessageDTO.getModelUriId()))
                .findFirst();

        // Определяем, существует ли такой чат в бд
        Optional<ChatModelDTO> chatModelOpt = mockDataStore.chats.stream()
                .filter(chat -> Objects.equals(chat.getId(), newMessageDTO.getChatId()))
                .findFirst();

        // Определеяем, существует ли такой пользователь в бд
        Optional<UserModelDTO> userModelOpt = userRepository.mockResponseUser.stream()
                .filter(user -> Objects.equals(user.getId(), mockBackendConfig.getUserId()))
                .findFirst();

        if (neuralNetworkOpt.isEmpty() || chatModelOpt.isEmpty() || userModelOpt.isEmpty()) {
            throw new GlobalException("NO_EXISTED_MODEL","Не существующая модель для генерации или нет такого чата");
        }


        // Обновляем нейросеть в соответствии с той, что указано в сообщении
        mockDataStore.chats.stream()
                .filter(chat -> Objects.equals(chat.getId(), newMessageDTO.getChatId()))
                .findFirst().get().setModelUriId(newMessageDTO.getModelUriId());

        // Обновление работает пока только по смотря по выбранной нейросети, это моковый вариант
        boolean updateMemory = false;
        String memory = userModelOpt.get().getMemory();
        if (newMessageDTO.getModelUriId()==1) {
            memory = memory + "абвгд+: ";
            userRepository.mockResponseUser.get((int) (mockBackendConfig.getUserId()-1)).setMemory(memory);
            updateMemory = true;
        }

        // Получаем все сообщения чата
        List<MessageModelDTO> messages = messageRepository.getAllChatMessagesByChatId(uidFirebase, newMessageDTO.getChatId());
        List<MessageForGenerator> generatorMessages = new ArrayList<>();
        for (MessageModelDTO message : messages) {
            generatorMessages.add(new MessageForGenerator(
                    message.getRole(),
                    message.getText()
            ));
        }
        // Собираем все настройки в сервисе, чтобы репозиторий генератора разбирался только с отправкой сообщения
        String networkName = neuralNetworkOpt.get().getSystemName();
        double temperature = chatModelOpt.get().getTemperature();
        String context = chatModelOpt.get().getContext();
        // Контекст пока в корявом виде только для мока, потом будет полная реализация вместе с настройками приложения
        context+=memory;
        // Отправляем в репозиторий генератора запрос на генерацию и получаем ответ
        ResponseGeneratedMessageDTO responseGeneratedMessageDTO = generatorRepository.generateMessage(networkName,generatorMessages,context,temperature);

        // Сохранение сообщения пользователя
        SaveMessageDTO saveUserMessageDTO = new SaveMessageDTO(
                chatModelOpt.get().getId(),
                null,
                false,
                "USER",
                newMessageDTO.getText(),
                "MESSAGE"
        );

        SaveMessageDTO saveNeuroMessageDTO = new SaveMessageDTO(
                chatModelOpt.get().getId(),
                newMessageDTO.getModelUriId(),
                false,
                "ASSISTIANT",
                responseGeneratedMessageDTO.getMessage().getText(),
                "MESSAGE"
        );
        messageRepository.createNewMessage(uidFirebase, chatModelOpt.get().getId(), saveUserMessageDTO);

        // Сохраняем ответ от нейросети в чате в бд
        MessageModelDTO messageNeuroModelDTO = messageRepository.createNewMessage(uidFirebase, chatModelOpt.get().getId(), saveNeuroMessageDTO);
        userRepository.mockResponseUser.get((int) (mockBackendConfig.getUserId()-1))
                .setMoney(userRepository.mockResponseUser.get((int) (mockBackendConfig.getUserId()-1))
                        .getMoney()-responseGeneratedMessageDTO.getUsage().getCost());

        // Возввращаем ответ
        MessageGenerateResponse response = new MessageGenerateResponse(
                new MessageGenerateResponse.MessageRequest(
                        messageNeuroModelDTO.getId(),
                        messageNeuroModelDTO.getText(),
                        messageNeuroModelDTO.getDateCreate(),
                        true
                ),
                new MessageGenerateResponse.User(
                        updateMemory,
                        memory,
                        responseGeneratedMessageDTO.getUsage().getCost(),
                        userRepository.mockResponseUser.get((int) (mockBackendConfig.getUserId()-1)).getMoney()
                )
        );
        logger.info("Created message: " + response.toString());
        return response;
    }

    //TODO Доделать позже
    @Override
    public MessageGenerateResponse regenerateMessage(String uidFirebase, RegenerateMessageDTO regenerateMessageDTO) {
        return null;
    }

    @Override
    public List<MessageModelDTO> newGetMessagesByChatId(String uidFirebase, Long chatId) throws InterruptedException {

        // Имитация ожидания запроса
        Thread.sleep(mockBackendConfig.getDelay());

        return messageRepository.getAllChatMessagesByChatId(uidFirebase, chatId);
    }

    //TODO Доделать позже
    @Override
    public SuccessDeleteMessagesDTO deleteMessagesByMessageId(String uidFirebase, DeleteMessagesDTO deleteMessagesDTO) {

        return null;
    }

    @Autowired
    private GeneratorRepository generatorRepository;
    @Autowired
    private MockNeuralNetworkRepository mockNeuralNetworkRepository;
    @Autowired
    private MockChatRepository mockChatRepository;
    @Autowired
    private MockUserRepository userRepository;
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserService userService;

}
