package com.promts.promts_test_server.promts_db.contoller;

import com.promts.promts_test_server.promts_db.dto.user.outbound.AppUserDto;
import com.promts.promts_test_server.promts_db.service.user.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/db/users")
@RequiredArgsConstructor
public class AppUserController {

    private final AppUserService service;

    @GetMapping("/uid/{uidFirebase}")
    public AppUserDto oneByUidFirebase(@PathVariable String uidFirebase) {
        return service.findByUidFirebase(uidFirebase);
    }

    @PostMapping
    public ResponseEntity<AppUserDto> create(@RequestBody AppUserDto dto) {
        AppUserDto saved = service.createUser(dto);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/uid/{uidFirebase}")
    public AppUserDto update(@PathVariable String uidFirebase, @RequestBody AppUserDto dto) {
        dto.setUidFirebase(uidFirebase);
        return service.updateUserSettings(dto);
    }
}
