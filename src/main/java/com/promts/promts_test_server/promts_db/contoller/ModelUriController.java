package com.promts.promts_test_server.promts_db.contoller;

import com.promts.promts_test_server.promts_db.dto.modeluri.outbound.ModelUriDto;
import com.promts.promts_test_server.promts_db.service.modeluri.ModelUriService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/db/models")
@RequiredArgsConstructor
public class ModelUriController {

    private final ModelUriService service;

    @GetMapping
    public List<ModelUriDto> all() {
        return service.findAll();
    }
}
