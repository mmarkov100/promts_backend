package com.promts.promts_test_server.service.Message;

import com.promts.promts_test_server.dto.Chat.inbound.ChatModelDTO;
import com.promts.promts_test_server.dto.Generator.inbound.ResponseGeneratedMessageDTO;
import com.promts.promts_test_server.dto.Generator.outbound.MessageForGenerator;
import com.promts.promts_test_server.dto.Message.inbound.DeleteMessagesDTO;
import com.promts.promts_test_server.dto.Message.inbound.MessageModelDTO;
import com.promts.promts_test_server.dto.Message.inbound.NewMessageDTO;
import com.promts.promts_test_server.dto.Message.inbound.RegenerateMessageDTO;
import com.promts.promts_test_server.dto.Message.outbound.ListMessages;
import com.promts.promts_test_server.dto.Message.outbound.MessageGenerateResponse;
import com.promts.promts_test_server.dto.Message.outbound.SaveMessageDTO;
import com.promts.promts_test_server.dto.Message.outbound.SuccessDeleteMessagesDTO;
import com.promts.promts_test_server.dto.NeuralNetwork.outbound.NeuralNetworkDTO;
import com.promts.promts_test_server.dto.User.inbound.UserInfoFromModelDTO;
import com.promts.promts_test_server.exception.GlobalException;
import com.promts.promts_test_server.repository.Chat.MockChatRepository;
import com.promts.promts_test_server.repository.Generator.GeneratorRepository;
import com.promts.promts_test_server.repository.Message.MessageRepository;
import com.promts.promts_test_server.repository.Message.MockMessageRepository;
import com.promts.promts_test_server.repository.NeuralNetwork.MockNeuralNetworkRepository;
import com.promts.promts_test_server.repository.User.MockUserRepository;
import com.promts.promts_test_server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Profile("mock")
public class MockMessageService implements MessageService{
    @Override
    public MessageGenerateResponse generateMessage(String uidFirebase, Long id, NewMessageDTO newMessageDTO) throws InterruptedException {

        Optional<NeuralNetworkDTO> neuralNetworkOpt = mockNeuralNetworkRepository.mockNeuralNetworkDTOS.stream()
                .filter(neuro -> Objects.equals(neuro.getId(), newMessageDTO.getModelUriId()))
                .findFirst();

        Optional<ChatModelDTO> chatModelOpt = mockChatRepository.mockResponseChats.stream()
                .filter(chat -> Objects.equals(chat.getId(), newMessageDTO.getChatId()))
                .findFirst();

        Optional<UserInfoFromModelDTO> userModelOpt = mockUserRepository.mockResponseUserWithChatsAndNeuros.stream()
                .filter(user -> Objects.equals(user.getUser().getId(), id))
                .findFirst();

        if (neuralNetworkOpt.isEmpty() || chatModelOpt.isEmpty() || userModelOpt.isEmpty()) {
            throw new GlobalException("NO_EXISTED_MODEL","Не существующая модель для генерации или нет такого чата");
        }

        // Обновление работает пока только по смотря по выбранной нейросети, это моковый вариант
        boolean updateMemory = false;
        String memory = userModelOpt.get().getUser().getMemory();
        if (newMessageDTO.getModelUriId()==1) {
            memory = memory + "абвгд+: ";
            mockUserRepository.mockResponseUserWithChatsAndNeuros.get((int) (id - 1)).getUser().setMemory(memory);
            updateMemory = true;
        }

        // Сохранение сообщения пользователя
        SaveMessageDTO saveUserMessageDTO = new SaveMessageDTO(
                chatModelOpt.get().getId(),
                null,
                false,
                "USER",
                newMessageDTO.getText(),
                "MESSAGE"
        );
        messageRepository.createNewMessage(uidFirebase, id, chatModelOpt.get().getId(), saveUserMessageDTO);

        List<MessageModelDTO> messages = messageRepository.getAllChatMessagesByChatId(uidFirebase, id, newMessageDTO.getChatId());
        List<MessageForGenerator> generatorMessages = new ArrayList<>();
        for (MessageModelDTO message : messages) {
            generatorMessages.add(new MessageForGenerator(
                    message.getRole(),
                    message.getText()
            ));
        }
        // Собираем все настройки в сервисе, чтобы репозиторий разбирался только с отправкой сообщения
        String networkName = neuralNetworkOpt.get().getName();
        double temperature = chatModelOpt.get().getTemperature();
        String context = chatModelOpt.get().getContext();
        // Контекст пока в корявом виде только для мока, потом будет полная реализация вместе с настройками приложения
        context+=memory;
        ResponseGeneratedMessageDTO responseGeneratedMessageDTO = generatorRepository.generateMessage(networkName,generatorMessages,context,temperature);
        SaveMessageDTO saveNeuroMessageDTO = new SaveMessageDTO(
                chatModelOpt.get().getId(),
                newMessageDTO.getModelUriId(),
                false,
                "ASSISTIANT",
                responseGeneratedMessageDTO.getMessage().getText(),
                "MESSAGE"
        );

        MessageModelDTO messageNeuroModelDTO = messageRepository.createNewMessage(uidFirebase, id, chatModelOpt.get().getId(), saveNeuroMessageDTO);
        mockUserRepository.mockResponseUserWithChatsAndNeuros.get((int) (id - 1)).getUser()
                .setMoney(mockUserRepository.mockResponseUserWithChatsAndNeuros.get((int) (id - 1)).getUser()
                        .getMoney()-responseGeneratedMessageDTO.getUsage().getCost());
        return new MessageGenerateResponse(
                new MessageGenerateResponse.MessageRequest(
                        messageNeuroModelDTO.getId(),
                        messageNeuroModelDTO.getText(),
                        messageNeuroModelDTO.getDateCreate(),
                        true
                ),
                new MessageGenerateResponse.User(
                        updateMemory,
                        memory,
                        mockUserRepository.mockResponseUserWithChatsAndNeuros.get((int) (id - 1)).getUser().getMoney(),
                        responseGeneratedMessageDTO.getUsage().getCost()
                )
        );

    }

    @Override
    public MessageGenerateResponse regenerateMessage(String uidFirebase, Long id, RegenerateMessageDTO regenerateMessageDTO) {
        return null;
    }

    @Override
    public ListMessages getMessagesByChatId(String uidFirebase, Long id, Long chatId) throws InterruptedException {

        ListMessages listMessages = new ListMessages();
        List<ListMessages.Message> list = new ArrayList<>();
        List<MessageModelDTO> messages = messageRepository.getAllChatMessagesByChatId(uidFirebase, id, chatId);

        for (MessageModelDTO message : messages) {
            list.add(new ListMessages.Message(
                    message.getId(),
                    message.getText(),
                    message.getRole(),
                    message.getDateCreate()
            ));
        }

        listMessages.setMessages(list);

        return listMessages;
    }

    @Override
    public SuccessDeleteMessagesDTO deleteMessagesByMessageId(String uidFirebase, Long id, DeleteMessagesDTO deleteMessagesDTO) {
        return null;
    }

    @Autowired
    private GeneratorRepository generatorRepository;
    @Autowired
    private MockNeuralNetworkRepository mockNeuralNetworkRepository;
    @Autowired
    private MockChatRepository mockChatRepository;
    @Autowired
    private MockUserRepository mockUserRepository;
    @Autowired
    private MockMessageRepository mockMessageRepository;
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserService userService;

}
