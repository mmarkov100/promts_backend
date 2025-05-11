package com.promts.promts_test_server.promts_generator.core.controller;


import com.promts.promts_test_server.promts_generator.core.dto.inbound.RequestGeneratorDTO;
import com.promts.promts_test_server.promts_generator.core.dto.outbound.ResponseDTO;
import com.promts.promts_test_server.shared.exception.GlobalException;
import com.promts.promts_test_server.promts_generator.features.openrouter.service.GPT4oMiniService;
import com.promts.promts_test_server.promts_generator.features.openrouter.service.GPTo4MiniHighService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/generator")
public class AppGeneratorController {

    private static final Logger logger = LoggerFactory.getLogger(AppGeneratorController.class);

    @Autowired
    private GPT4oMiniService gpt4oMiniService;
    @Autowired
    private GPTo4MiniHighService gpto4MiniHighService;

    @GetMapping("/health")
    public Map<String, String> healthCheck() {
        return Map.of("status", "UP");
    }

    @PostMapping("/generate")
    public ResponseEntity<?> generateMessage(@Valid @RequestBody RequestGeneratorDTO requestGeneratorDTO){

        logger.info("Got request to generate using model: {}", requestGeneratorDTO.toString());

        ResponseDTO result;
        return switch (requestGeneratorDTO.getModel()) {
            case "openai/gpt-4o-mini" -> {
                result = gpt4oMiniService.serviceMessage(requestGeneratorDTO);
                logger.info("result: {}", result.toString());
                yield ResponseEntity.ok().body(result);
            }
            case "openai/o4-mini-high" -> {
                result = gpto4MiniHighService.serviceMessage(requestGeneratorDTO);
                logger.info("result: {}", result.toString());
                yield ResponseEntity.ok().body(result);
            }
            default -> throw new GlobalException("NOT_EXISTED_MODEL", "Такой модели для генерации нет");
        };

    }
}
