package com.promts.promts_test_server.repository.Generator;

/*
Этот репозиторий отвечает за обращение в сервис для генерации сообщений
 */

import com.promts.promts_test_server.dto.Generator.inbound.ResponseGeneratedMessageDTO;
import com.promts.promts_test_server.dto.Generator.inbound.ResponseGeneratedVectorDTO;
import com.promts.promts_test_server.dto.Generator.outbound.MessageForGenerator;

import java.util.List;

public interface GeneratorRepository {
    ResponseGeneratedMessageDTO generateMessage(String modelUri, List<MessageForGenerator> text, String context, double temperature) throws InterruptedException;
    ResponseGeneratedVectorDTO generateVector(String modelUri, String text);
}
