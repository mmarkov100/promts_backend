package com.promts.promts_test_server.promts_db.contoller;

import com.promts.promts_test_server.promts_db.dto.chat.outbound.ChatDto;
import com.promts.promts_test_server.promts_db.entity.chat.Chat;
import com.promts.promts_test_server.promts_db.service.chat.ChatServiceDB;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/db/chat")
@RequiredArgsConstructor
public class ChatControllerDB {

    private ChatServiceDB chatServiceDB;

//    @GetMapping("/all")
    public List<Chat> getChats(String uidFirebase){

        return chatServiceDB.getChats(uidFirebase);
    }

//    @PostMapping("/create")
    public Chat create(String uidFirebase, ChatDto dto){

        return chatServiceDB.create(uidFirebase,dto);
    }

//    @PutMapping("/update")
    public Chat update(String uidFirebase, ChatDto dto) {

        return chatServiceDB.update(uidFirebase,dto);
    }

//    @DeleteMapping("/delete")
    public void delete(String uidFirebase, Long chatId) {
        chatServiceDB.delete(uidFirebase, chatId);
    }
}
