package com.promts.promts_test_server.promts_backend.repository.Generator;

import com.promts.promts_test_server.promts_backend.dto.Generator.inbound.ResponseGeneratedMessageDTO;
import com.promts.promts_test_server.promts_backend.dto.Generator.inbound.ResponseGeneratedVectorDTO;
import com.promts.promts_test_server.promts_backend.dto.Generator.outbound.MessageForGenerator;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Profile("model")
public class ModelGeneratorRepository implements GeneratorRepository{
    @Override
    public ResponseGeneratedMessageDTO generateMessage(String modelUri, List<MessageForGenerator> text, String context, double temperature) {
        return null;
    }

    @Override
    public ResponseGeneratedVectorDTO generateVector(String modelUri, String text) {
        return null;
    }
}
