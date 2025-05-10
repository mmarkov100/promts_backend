package com.promts.promts_test_server.promts_db.service.modeluri;

import com.promts.promts_test_server.promts_db.dto.modeluri.outbound.ModelUriDto;
import com.promts.promts_test_server.promts_db.entity.modeluri.ModelUri;
import com.promts.promts_test_server.promts_db.repository.ModelUriRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ModelUriService {

    private final ModelUriRepository repo;

    public List<ModelUriDto> findAll() {
        return repo.findAll().stream().map(this::toDto).toList();
    }

    /* ------- маппинг -------- */
    private ModelUriDto toDto(ModelUri e) {
        return ModelUriDto.builder()
                .id(e.getId())
                .name(e.getName())
                .systemName(e.getSystemName())
                .desc(e.getDescription())
                .build();
    }
}
