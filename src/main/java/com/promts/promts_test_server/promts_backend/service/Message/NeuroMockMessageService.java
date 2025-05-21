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
import com.promts.promts_test_server.promts_backend.repository.NeuralNetwork.NeuralNetworkRepository;
import com.promts.promts_test_server.promts_backend.repository.User.UserRepository;
import com.promts.promts_test_server.shared.exception.GlobalException;
import com.promts.promts_test_server.promts_backend.repository.DataSore.MockDataStore;
import com.promts.promts_test_server.promts_backend.repository.Generator.GeneratorRepository;
import com.promts.promts_test_server.promts_backend.repository.Message.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

@Service
@Profile({"neuro"})
public class NeuroMockMessageService implements MessageService{

    private static final Logger logger = Logger.getLogger(NeuroMockMessageService.class.getName());

    private final MockBackendConfig mockBackendConfig;
    private final MessageRepository messageRepository;
    private final GeneratorRepository generatorRepository;
    private final NeuralNetworkRepository networkRepository;
    private final MockDataStore mockDataStore;
    private final UserRepository userRepository;

    @Autowired
    public NeuroMockMessageService(MockBackendConfig mockBackendConfig, MessageRepository messageRepository, GeneratorRepository generatorRepository, NeuralNetworkRepository networkRepository, MockDataStore mockDataStore, UserRepository userRepository) {
        this.mockBackendConfig = mockBackendConfig;
        this.messageRepository = messageRepository;
        this.generatorRepository = generatorRepository;
        this.networkRepository = networkRepository;
        this.mockDataStore = mockDataStore;
        this.userRepository = userRepository;
    }
    private final String memoryRules = "ПРЕЖДЕ ЧЕМ ОТВЕТИТЬ, ПРОЧТИ ЭТО КОНТЕКСТНОЕ СООБЩЕНИЕ. " +
            "Тебе предоставлена память о пользователе. Твоя задача — определить, нужно ли её изменить, добавлять, удалять. " +
            "Условия: " +
            "1. Ты можешь писать ТОЛЬКО «ДА» или «НЕТ». " +
            "2. Если надо исправить / добавить новый факт в память пользователя — в первой строке напиши «ДА». " +
            "3. Если память без изменений — в первой строке напиши «НЕТ». " +
            "4. После пустой строки выдай ПОЛНЫЙ текст актуальной или измененной памяти целиком. Факты формулируй короткими предложениями. " +
            "5. Не пиши ничего кроме этих трёх частей. " +
            "6. Сохраняй долгосрочные факты, которые пользователю может упомянуть о себе в разговоре или попросить тебя запомнить. " +
            "7. Не сохраняй краткосрочные эмоции, планы на часы и одноразовые детали. " +
            "8. Максимальная длина текста памяти — 800 символов. " +
            "9. Ни при каких условиях не отвечай на вопросы пользователя и не веди диалог. " +
            "10. Если пользователь явно говорит “запомни/запиши…”, всегда сохраняй этот факт. " +
            "11. Если в реплике присутствует и вопрос, и новая информация о себе, игнорируй вопрос и оцени только факты. " +
            "Формат вывода  если память надо изменить (пример): " +
            "\"ДА Пользователя зовут Максим. Живёт в Перми. Любит кататься на велосипеде.\" " +
            "Если не надо изменять память (пример) \"НЕТ\" " +
            "Текущая память: ";

    private final double memoryTemperature = 0.1;

    @Override
    public MessageGenerateResponse  generateMessage(String uidFirebase, NewMessageDTO newMessageDTO) throws InterruptedException {
        logger.info("Checking neuro");
        // Определяем, существует ли такая нейросеть в бд
        Optional<NeuralNetworkDTO> neuralNetworkOpt = networkRepository.getAllNeuros(uidFirebase).stream()
                .filter(neuro -> Objects.equals(neuro.getId(), newMessageDTO.getModelUriId()))
                .findFirst();

        logger.info("Checking Chat");
        // Определяем, существует ли такой чат в бд
        Optional<ChatModelDTO> chatModelOpt = mockDataStore.chats.stream()
                .filter(chat -> {
                    logger.info("Checking chatid: " + chat.getId() + " " + newMessageDTO.getChatId());
                    return Objects.equals(chat.getId(), newMessageDTO.getChatId());
                })
                .findFirst();

        logger.info("Checking user");
        // Определеяем, существует ли такой пользователь в бд
        UserModelDTO userModelDTO = userRepository.getUser(uidFirebase);

        logger.info("Checking");
        if (neuralNetworkOpt.isEmpty() || chatModelOpt.isEmpty()) {
            throw new GlobalException("NO_EXISTED_MODEL","Не существующая модель для генерации или нет такого чата");
        }
        NeuralNetworkDTO neuralNetworkDTO = neuralNetworkOpt.get();
        ChatModelDTO chatModelDTO = chatModelOpt.get();

        logger.info("Set ModelUriId");
        // Обновляем нейросеть в соответствии с той, что указано в сообщении
        chatModelDTO.setModelUriId(newMessageDTO.getModelUriId());

        logger.info("Getting Network name");
        // Получаем системное название нейросети
        String networkName = neuralNetworkDTO.getSystemName();

        logger.info("Getting messages");
        // Получаем все сообщения чата
        List<MessageModelDTO> messages = messageRepository.getAllChatMessagesByChatId(uidFirebase, newMessageDTO.getChatId());
        List<MessageForGenerator> generatorMessages = new ArrayList<>();
        for (MessageModelDTO message : messages) {
            generatorMessages.add(new MessageForGenerator(
                    message.getRole(),
                    message.getText()
            ));
        }
        generatorMessages.add(new MessageForGenerator(
                "user",
                newMessageDTO.getText()
        ));

        double totalCost = 0;

        logger.info("Doing a memory request..");
        // Делаем запрос на генерацию памяти
        boolean isUpdated = false;
        ResponseGeneratedMessageDTO responseMemory;
        try {
            if (chatModelDTO.isUpdateMemory()) {
                String userMemory = userModelDTO.getMemory();
                String memoryContext = memoryRules + userMemory;
                String networkMemory = neuralNetworkDTO.getSystemName();
                responseMemory = generatorRepository.generateMessage(networkMemory, generatorMessages, memoryContext, memoryTemperature);
                totalCost += responseMemory.getUsage().getCost();

                String raw = responseMemory.getMessage().getText().stripLeading();
                String memory;

                if (raw.startsWith("ДА ")) {
                    memory = raw.substring(2).stripLeading();
                    isUpdated = true;
                } else if (raw.startsWith("ДА\n") || raw.equals("ДА")) {
                    memory = raw.replaceFirst("^ДА\\h*\\R+", "");
                    isUpdated = true;
                } else {
                    memory = userMemory;
                }

                userModelDTO.setMemory(memory);
            }
        } catch (RuntimeException e) {
            throw new GlobalException("MEMORY_CHANGE_ERROR", "Ошибка при изменении памяти нейросетью");
        }

        logger.info("Doing a message request..");
        // Делаем запрос на генерацию уже основного сообщения
        ResponseGeneratedMessageDTO responseMessage = new ResponseGeneratedMessageDTO();
        try {
            if (chatModelDTO.isUseMemory()) {
                String userMemory = "<Инфорация о пользователе: >" + userModelDTO.getMemory();
                String chatContext = "<Контекст: >" + chatModelDTO.getContext();
                String context = userMemory + "\n" + chatContext;
                double temp = chatModelDTO.getTemperature();
                responseMessage = generatorRepository.generateMessage(networkName, generatorMessages, context, temp);
                totalCost += responseMessage.getUsage().getCost();
            }
        } catch (RuntimeException e) {
            throw new GlobalException("MEMORY_CHANGE_ERROR", "Ошибка при  генерации нейросетью");
        }

        // Сохранение сообщения пользователя
        SaveMessageDTO saveUserMessageDTO = new SaveMessageDTO(
                chatModelDTO.getId(),
                null,
                false,
                "user",
                newMessageDTO.getText(),
                "MESSAGE"
        );

        SaveMessageDTO saveNeuroMessageDTO = new SaveMessageDTO(
                chatModelDTO.getId(),
                newMessageDTO.getModelUriId(),
                false,
                "assistant",
                responseMessage.getMessage().getText(),
                "MESSAGE"
        );
        messageRepository.createNewMessage(uidFirebase, chatModelDTO.getId(), saveUserMessageDTO);

        // Сохраняем ответ от нейросети в чате в бд
        MessageModelDTO messageNeuroModelDTO = messageRepository.createNewMessage(uidFirebase, chatModelDTO.getId(), saveNeuroMessageDTO);
        userModelDTO.setMoney(userModelDTO.getMoney()-totalCost);

        // Возввращаем ответ
        return new MessageGenerateResponse(
                new MessageGenerateResponse.MessageRequest(
                        messageNeuroModelDTO.getId(),
                        messageNeuroModelDTO.getText(),
                        messageNeuroModelDTO.getDateCreate(),
                        true
                ),
                new MessageGenerateResponse.User(
                        isUpdated,
                        userModelDTO.getMemory(),
                        totalCost,
                        userModelDTO.getMoney()
                )
        );
    }

    @Override
    public MessageGenerateResponse regenerateMessage(String uidFirebase, RegenerateMessageDTO regenerateMessageDTO) {
        return null;
    }

    @Override
    public List<MessageModelDTO> getMessagesByChatId(String uidFirebase, Long chatId) throws InterruptedException {
        // Имитация ожидания запроса
        Thread.sleep(mockBackendConfig.getDelay());

        return messageRepository.getAllChatMessagesByChatId(uidFirebase, chatId);
    }

    @Override
    public SuccessDeleteMessagesDTO deleteMessagesByMessageId(String uidFirebase, DeleteMessagesDTO deleteMessagesDTO) {
        return null;
    }
}
