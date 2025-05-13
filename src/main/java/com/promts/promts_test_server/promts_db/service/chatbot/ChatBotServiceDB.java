package com.promts.promts_test_server.promts_db.service.chatbot;

import com.promts.promts_test_server.promts_backend.dto.ChatBot.inbound.CreateChatBotDTO;
import com.promts.promts_test_server.promts_db.entity.chatbot.ChatBot;
import com.promts.promts_test_server.promts_db.repository.AppUserRepository;
import com.promts.promts_test_server.promts_db.repository.ChatBotRepository;
import com.promts.promts_test_server.promts_db.repository.ModelUriRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatBotServiceDB {

    private final ChatBotRepository repo;
    private final ModelUriRepository modelRepo;
    private final AppUserRepository userRepo;

    /** Получить ботов для каталога / поиска. */
    public List<ChatBot> findAll(){
        return repo.findAll();
    }

    /** Карточка одного бота. */
    public ChatBot findById(Long id) {
        return repo.findById(id).orElseThrow();
    }

    //TODO ПЕРЕДЕЛАТЬ
    /** Создание нового бота текущим пользователем. */
    public ChatBot create(CreateChatBotDTO dto, String uidFirebase) {
        ChatBot bot = ChatBot.builder()
                .chatBotName(dto.getChatBotName())
                .chatBotDesc(dto.getChatBotDesc())
                .helloMessage(dto.getHelloMessage())
                .temperature(dto.getTemperature())
                .context(dto.getContext())
                .canUseMemory(dto.isCanUseMemory())
                .canUpdateMemory(dto.isCanUpdateMemory())
                .canEditModelUri(dto.isCanEditModelUri())
                .canEditContext(dto.isCanEditContext())
                .modelUri(modelRepo.getReferenceById(dto.getModelUriId()))
                .user(userRepo.findByUidFirebase(uidFirebase).orElse(null))
                .build();
        return repo.save(bot);
    }
}
