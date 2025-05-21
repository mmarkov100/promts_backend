package com.promts.promts_test_server.promts_backend.service.Message;

import com.promts.promts_test_server.promts_backend.dto.Generator.inbound.ResponseGeneratedMessageDTO;
import com.promts.promts_test_server.promts_backend.dto.Generator.outbound.MessageForGenerator;
import com.promts.promts_test_server.promts_backend.dto.Message.inbound.DeleteMessagesDTO;
import com.promts.promts_test_server.promts_backend.dto.Message.inbound.MessageModelDTO;
import com.promts.promts_test_server.promts_backend.dto.Message.inbound.NewMessageDTO;
import com.promts.promts_test_server.promts_backend.dto.Message.outbound.MessageGenerateResponse;
import com.promts.promts_test_server.promts_backend.dto.Message.outbound.SuccessDeleteMessagesDTO;
import com.promts.promts_test_server.promts_db.dto.message.outbound.MessageDbDto;
import com.promts.promts_test_server.promts_db.dto.user.outbound.AppUserDto;
import com.promts.promts_test_server.promts_db.entity.app_settings.AppSettings;
import com.promts.promts_test_server.promts_db.entity.chat.Chat;
import com.promts.promts_test_server.promts_db.entity.message.Message;
import com.promts.promts_test_server.promts_db.entity.modeluri.ModelUri;
import com.promts.promts_test_server.promts_db.service.chat.ChatServiceDB;
import com.promts.promts_test_server.promts_db.service.modeluri.ModelUriService;
import com.promts.promts_test_server.promts_db.service.settings.AppSettingsService;
import com.promts.promts_test_server.promts_db.service.user.AppUserService;
import com.promts.promts_test_server.shared.exception.GlobalException;
import com.promts.promts_test_server.promts_backend.repository.Generator.GeneratorRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;


@Service
@Profile({"dbmock"})
public class DBMockMessageService implements MessageService {

    private static final Logger logger = Logger.getLogger(DBMockMessageService.class.getName());

    private final GeneratorRepository generatorRepository;

    private final com.promts.promts_test_server.promts_db.service.message.MessageService dbMessageService;
    private final AppUserService appUserService;
    private final ModelUriService modelUriService;
    private final ChatServiceDB chatServiceDB;
    private final AppSettingsService settingsService;

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

    @Autowired
    public DBMockMessageService(
            GeneratorRepository generatorRepository,
            com.promts.promts_test_server.promts_db.service.message.MessageService dbMessageService,
            AppUserService appUserService,
            ModelUriService modelUriService,
            ChatServiceDB chatServiceDB,
            AppSettingsService settingsService) {
        this.generatorRepository = generatorRepository;
        this.dbMessageService = dbMessageService;
        this.appUserService = appUserService;
        this.modelUriService = modelUriService;
        this.chatServiceDB = chatServiceDB;
        this.settingsService = settingsService;
    }

    @Override
    public MessageGenerateResponse generateMessage(String uidFirebase, NewMessageDTO newMessageDTO) throws InterruptedException {
        logger.info("Запрос на генерацию сообщения для пользователя: " + uidFirebase + ", чат: " + newMessageDTO.getChatId());

        // 1. Получаем данные из БД через DB сервисы
        AppUserDto userDto = appUserService.findByUidFirebase(uidFirebase);
        logger.info("Найден пользователь");
        // Важно: ChatServiceDB должен иметь метод для получения ChatDto или Chat entity по ID и uidFirebase (для проверки прав)
        // Пока предположим, что такой метод есть или мы получаем все чаты и фильтруем
        // Для примера, получим Chat entity, чтобы иметь доступ ко всем полям.
        // Необходимо добавить в ChatServiceDB метод вроде findByIdAndUserUidFirebase(Long chatId, String uidFirebase)
        AppSettings appSettings = settingsService.get();
        logger.info("Найдены настройки");

        Chat chatEntity = chatServiceDB.getChats(uidFirebase)
                .stream()
                .filter(c -> c.getId().equals(newMessageDTO.getChatId()))
                .findFirst()
                .orElseThrow(() -> new GlobalException("CHAT_NOT_FOUND_OR_ACCESS_DENIED", "Чат не найден или доступ запрещен"));
        logger.info("Найден чат");

        ModelUri modelUriEntity = modelUriService.findAll()
                .stream()
                .filter(m -> m.getId().equals(newMessageDTO.getModelUriId()))
                .findFirst()
                .orElseThrow(() -> new GlobalException("MODEL_NOT_FOUND", "Модель для генерации не найдена"));
        logger.info("Найдена нейросеть для генерации");

        ModelUri modelUriMemory = modelUriService.findAll()
                .stream()
                .filter(m -> m.getId().equals(appSettings.getStandardModelUriMemory().getId()))
                .findFirst()
                .orElseThrow(() -> new GlobalException("MODEL_NOT_FOUND", "Модель для генерации не найдена"));
        logger.info("Найдены нейросеть для памяти");

        // Обновляем ModelUriId в чате, если он изменился (это должно делаться через ChatServiceDB)
        if (!chatEntity.getModelUri().getId().equals(newMessageDTO.getModelUriId())) {
            Chat newchat = chatServiceDB.updateChatModelUri(chatEntity.getId(), newMessageDTO.getModelUriId(), uidFirebase);
            chatEntity.setModelUri(newchat.getModelUri());
        }
        logger.info("Позже обновить нейросеть в чате");
        
        // Сохраняем сообщение пользователя в БД
        MessageDbDto userMessageDbDto = MessageDbDto.builder()
                .chatId(chatEntity.getId())
                .role("USER")
                .text(newMessageDTO.getText())
                .type("MESSAGE")
                .oldMessage(false)
                .modelUriId(null)
                .build();
        // Используем dbMessageService.savePureMessage, если не хотим двойного логирования транзакции
        // или если dbMessageService.send автоматически не создает транзакцию для USER сообщений
        dbMessageService.savePureMessage(userMessageDbDto);
        logger.info("Сохранено сообщение пользователя в чате");


        // 2. Формируем историю сообщений для генератора
        List<Message> dbMessages =
            dbMessageService.history(uidFirebase, chatEntity.getId(), false);
        logger.info("Найдены сообщения для генератора");

        List<MessageForGenerator> generatorMessages = dbMessages.stream()
                .map(dbMsg -> new MessageForGenerator(dbMsg.getRole().toLowerCase(), dbMsg.getText()))
                .collect(Collectors.toList());
        logger.info("Сформированы сообщения для генератора");
        // Добавляем текущее сообщение пользователя, которое еще не в dbMessages (или уже, если history вызывается после сохранения)
        // generatorMessages.add(new MessageForGenerator("USER", newMessageDTO.getText())); // Это уже сделано выше через savePureMessage и history

        BigDecimal totalCostForResponse = BigDecimal.ZERO;
        String finalUserMemory = userDto.getMemory();
        boolean memoryWasUpdated = false;

        // 3. Логика изменения памяти (если включено в чате и у пользователя)
        if (chatEntity.isUpdateMemory() && userDto.isAiCanUpdateMemory() && userDto.isMemoryEnabled()) {
            logger.info("Попытка обновления памяти для чата: " + chatEntity.getId());
            String memoryContext = memoryRules + (userDto.getMemory() == null ? "" : userDto.getMemory());
            // Используем модель по умолчанию для памяти или специальную, если она настроена
            // com.promts.promts_test_server.promts_db.entity.modeluri.ModelUri memoryModel = ...; // Загрузить модель для памяти
            // Пока используем ту же модель, что и для чата, но лучше иметь отдельную настройку
            
            // Предположим, у вас есть AppSettingsService для получения стандартной модели памяти
            // AppSettings appSettings = appSettingsService.get();
            // ModelUri memoryModel = appSettings.getStandardModelUriMemory();
            // String memoryModelSystemName = memoryModel.getSystemName();
            // BigDecimal memoryModelCostPerToken = ... ; // Нужно знать стоимость токенов модели памяти

            // Пока что используем модель чата для генерации памяти для упрощения
            String memoryModelSystemName = modelUriMemory.getSystemName();
            logger.info("Нашли системное название нейросети для памяти");


            ResponseGeneratedMessageDTO memoryGenResponse = generatorRepository.generateMessage(
                    memoryModelSystemName,
                    generatorMessages,
                    memoryContext,
                    memoryTemperature);
            logger.info("Получили ответ для памяти");

            if (memoryGenResponse != null && memoryGenResponse.getUsage() != null) {
                BigDecimal inputTokenCost = modelUriEntity.getCostInputTokensRub() != null ? modelUriEntity.getCostInputTokensRub() : BigDecimal.ZERO;
                BigDecimal outputTokenCost = modelUriEntity.getCostOutputTokensRub() != null ? modelUriEntity.getCostOutputTokensRub() : BigDecimal.ZERO;
                logger.info("Получили стоимость: " + inputTokenCost + " " + outputTokenCost);
                
                BigDecimal costOfMemoryUpdate = BigDecimal.ZERO;
                if (memoryGenResponse.getUsage().getInputTextTokens() > 0) {
                     costOfMemoryUpdate = costOfMemoryUpdate.add(
                        inputTokenCost.multiply(BigDecimal.valueOf(memoryGenResponse.getUsage().getInputTextTokens() / 1000.0))
                     );
                }
                if (memoryGenResponse.getUsage().getCompletionTokens() > 0) {
                     costOfMemoryUpdate = costOfMemoryUpdate.add(
                        outputTokenCost.multiply(BigDecimal.valueOf(memoryGenResponse.getUsage().getCompletionTokens() / 1000.0))
                     );
                }
                // Добавляем стоимость запроса, если она есть
                if (modelUriEntity.getCostRequestRub() != null) {
                    costOfMemoryUpdate = costOfMemoryUpdate.add(modelUriEntity.getCostRequestRub());
                }


                totalCostForResponse = totalCostForResponse.add(costOfMemoryUpdate);

                String rawMemoryResponse = memoryGenResponse.getMessage().getText().stripLeading();
                String newMemoryText;
                if (rawMemoryResponse.startsWith("ДА ")) {
                    newMemoryText = rawMemoryResponse.substring(3).stripLeading();
                    memoryWasUpdated = true;
                } else if (rawMemoryResponse.startsWith("ДА\n")) {
                    newMemoryText = rawMemoryResponse.substring(3).stripLeading(); // Удаляем "ДА" и перевод строки
                    memoryWasUpdated = true;
                } else if (rawMemoryResponse.equals("ДА")) { // Если только "ДА" без текста
                    newMemoryText = ""; // Память становится пустой или остается без изменений, если ДА означает "сохранить текущее"
                    memoryWasUpdated = true; // Считаем, что ДА - это изменение (даже если на пустую)
                }
                else { // "НЕТ" или что-то другое
                    newMemoryText = userDto.getMemory(); // Память не изменилась
                }
                finalUserMemory = newMemoryText;

                // Сохраняем сообщение об изменении памяти в БД
                MessageDbDto memoryMessageDbDto = MessageDbDto.builder()
                        .chatId(chatEntity.getId())
                        .role("SYSTEM")
                        .text(rawMemoryResponse)
                        .type("SYSTEM_MEMORY")
                        .oldMessage(false)
                        .modelUriId(modelUriMemory.getId())
                        .build();
                dbMessageService.send(uidFirebase, memoryMessageDbDto, costOfMemoryUpdate,
                        memoryGenResponse.getUsage().getInputTextTokens(),
                        memoryGenResponse.getUsage().getTotalTokens());

                // Обновляем память пользователя в БД
                // appUserService.updateUserMemory(uidFirebase, finalUserMemory); // Нужен такой метод в AppUserService
                // Пока что обновим через существующий updateUserDTO, создав его:
                if (memoryWasUpdated){
                    AppUserDto userToUpdate = appUserService.findByUidFirebase(uidFirebase);
                    userToUpdate.setMemory(finalUserMemory);
                    appUserService.updateUserSettings(userToUpdate);
                    logger.info("Память пользователя " + uidFirebase + " обновлена.");
                }

            }
        }

        // 4. Генерация ответа на сообщение пользователя
        String contextForGeneration = "";
        if (chatEntity.isUseMemory() && userDto.isMemoryEnabled() && finalUserMemory != null && !finalUserMemory.isEmpty()) {
            contextForGeneration += "<Информация о пользователе: >" + finalUserMemory + "\n";
        }
        if (chatEntity.getContext() != null && !chatEntity.getContext().isEmpty()) {
             contextForGeneration += "<Контекст чата: >" + chatEntity.getContext();
        }


        ResponseGeneratedMessageDTO mainGenResponse = generatorRepository.generateMessage(
                modelUriEntity.getSystemName(),
                generatorMessages, // История, включая текущее сообщение пользователя
                contextForGeneration.isEmpty() ? null : contextForGeneration,
                chatEntity.getTemperature());

        BigDecimal costOfMainMessage = BigDecimal.ZERO;
        if (mainGenResponse != null && mainGenResponse.getUsage() != null) {
            BigDecimal inputTokenCost = modelUriEntity.getCostInputTokensRub() != null ? modelUriEntity.getCostInputTokensRub() : BigDecimal.ZERO;
            BigDecimal outputTokenCost = modelUriEntity.getCostOutputTokensRub() != null ? modelUriEntity.getCostOutputTokensRub() : BigDecimal.ZERO;

            if (mainGenResponse.getUsage().getInputTextTokens() > 0) {
                 costOfMainMessage = costOfMainMessage.add(
                    inputTokenCost.multiply(BigDecimal.valueOf(mainGenResponse.getUsage().getInputTextTokens() / 1000.0))
                 );
            }
            if (mainGenResponse.getUsage().getCompletionTokens() > 0) {
                 costOfMainMessage = costOfMainMessage.add(
                    outputTokenCost.multiply(BigDecimal.valueOf(mainGenResponse.getUsage().getCompletionTokens() / 1000.0))
                 );
            }
            if (modelUriEntity.getCostRequestRub() != null) {
                 costOfMainMessage = costOfMainMessage.add(modelUriEntity.getCostRequestRub());
            }
            totalCostForResponse = totalCostForResponse.add(costOfMainMessage);
        }
        
        // 5. Сохранение сообщения ассистента в БД и транзакции
        assert mainGenResponse != null;
        MessageDbDto assistantMessageDbDto = MessageDbDto.builder()
                .chatId(chatEntity.getId())
                .role("ASSISTANT")
                .text(mainGenResponse.getMessage().getText())
                .type("MESSAGE")
                .oldMessage(false)
                .modelUriId(modelUriEntity.getId())
                .build();
        Message savedAssistantMessage = dbMessageService.send(
                uidFirebase, 
                assistantMessageDbDto, 
                costOfMainMessage, // Стоимость конкретно этого сообщения (без комиссии)
                mainGenResponse.getUsage().getInputTextTokens(), 
                mainGenResponse.getUsage().getTotalTokens()
        );

        // 6. Обновляем баланс пользователя (dbMessageService.send уже должен это делать через PaymentTransactionService)
        // Получаем актуальный баланс после всех операций
        AppUserDto updatedUserDto = appUserService.findByUidFirebase(uidFirebase);


        // 7. Формируем финальный ответ для фронтенда
        MessageGenerateResponse.MessageRequest messageRequestPart = new MessageGenerateResponse.MessageRequest(
                savedAssistantMessage.getId(),
                savedAssistantMessage.getText(),
                savedAssistantMessage.getDateCreate(), // Это будет время создания из БД
                true // stopped - всегда true для мока
        );

        MessageGenerateResponse.User userPart = new MessageGenerateResponse.User(
                memoryWasUpdated,
                finalUserMemory, // Актуальная память
                totalCostForResponse.doubleValue(), // Общая стоимость за все генерации в этом запросе (до применения комиссии)
                updatedUserDto.getMoney() // Актуальный баланс из БД
        );

        return new MessageGenerateResponse(messageRequestPart, userPart);
    }


    // --- Остальные методы интерфейса MessageService ---
    @Override
    public MessageGenerateResponse regenerateMessage(String uidFirebase, com.promts.promts_test_server.promts_backend.dto.Message.inbound.RegenerateMessageDTO regenerateMessageDTO) {
        // TODO: Реализовать логику регенерации сообщения
        logger.warning("Метод regenerateMessage не реализован для dbmock профиля.");
        throw new GlobalException("NOT_IMPLEMENTED", "Функция регенерации сообщения не реализована.");
    }

    @Override
    @Transactional(readOnly = true)
    public List<MessageModelDTO> getMessagesByChatId(String uidFirebase, Long chatId) {
        logger.info("Запрос истории сообщений для чата: " + chatId + ", пользователь: " + uidFirebase);
        
        List<Message> dbMessages = dbMessageService.history(uidFirebase, chatId, false);

        // Маппинг из DB сущности в Backend DTO
        return dbMessages.stream()
                .map(dbMsg -> new MessageModelDTO(
                        dbMsg.getId(),
                        dbMsg.getChat().getId(),
                        dbMsg.getModelUri() != null ? dbMsg.getModelUri().getId() : null,
                        dbMsg.isOldMessage(),
                        dbMsg.getRole(),
                        dbMsg.getText(),
                        dbMsg.getType(),
                        dbMsg.getDateCreate()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public SuccessDeleteMessagesDTO deleteMessagesByMessageId(String uidFirebase, DeleteMessagesDTO deleteMessagesDTO) {
        // TODO: Реализовать логику удаления сообщений (пометка oldMessage = true)
        logger.warning("Метод deleteMessagesByMessageId не реализован для dbmock профиля.");
        throw new GlobalException("NOT_IMPLEMENTED", "Функция удаления сообщений не реализована.");
    }
}