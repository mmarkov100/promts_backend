package com.promts.promts_test_server.promts_db.contoller;

import com.promts.promts_test_server.promts_db.dto.user.outbound.AppUserDto;
import com.promts.promts_test_server.promts_db.service.user.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class AppUserController {

    private final AppUserService service;

    @GetMapping("/uid/{uidFirebase}")
    public AppUserDto oneByUidFirebase(@PathVariable String uidFirebase) {
        return service.findByUidFirebase(uidFirebase);
    }

    @PostMapping
    public ResponseEntity<AppUserDto> create(@RequestBody AppUserDto dto) {
        AppUserDto saved = service.create(dto);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public AppUserDto update(@PathVariable Long id, @RequestBody AppUserDto dto) {
        dto.setId(id);
        return service.save(dto);
    }
}
