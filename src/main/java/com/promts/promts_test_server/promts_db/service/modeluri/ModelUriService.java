package com.promts.promts_test_server.promts_db.service.modeluri;

import com.promts.promts_test_server.promts_db.dto.modeluri.outbound.ModelUriDto;
import com.promts.promts_test_server.promts_db.entity.modeluri.ModelUri;
import com.promts.promts_test_server.promts_db.repository.ModelUriRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ModelUriService {

    private final ModelUriRepository repo;

    public List<ModelUriDto> findAllDto() {
        return repo.findAll().stream().map(this::toDto).toList();
    }

    public List<ModelUri> findAll(){
        return repo.findAll();
    }

    /* ------- маппинг -------- */
    public Optional<ModelUri> findBySystemName(String model) {
        return repo.findBySystemName(model);
    }

    private ModelUriDto toDto(ModelUri e) {
        return ModelUriDto.builder()
                .id(e.getId())
                .name(e.getName())
                .systemName(e.getSystemName())
                .desc(e.getDescription())
                .build();
    }
}
