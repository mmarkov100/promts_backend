package com.promts.promts_test_server.repository.Generator;

/*
Моковый генератор, хранит в себе заранее сгенерированные сообщения
 */

import com.promts.promts_test_server.config.MockConfig;
import com.promts.promts_test_server.dto.Generator.inbound.ResponseGeneratedMessageDTO;
import com.promts.promts_test_server.dto.Generator.inbound.ResponseGeneratedVectorDTO;
import com.promts.promts_test_server.dto.Generator.outbound.MessageForGenerator;
import com.promts.promts_test_server.exception.GlobalException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@Profile("mock")
public class MockGeneratorRepository implements GeneratorRepository{

    @Autowired
    private MockConfig mockConfig;

    // Пока сообщения выводятся в соответствии от выбранной нейросети, это сделано только для мока
    @Override
    public ResponseGeneratedMessageDTO generateMessage(String modelUri, List<MessageForGenerator> text, String context, double temperature) throws InterruptedException {

        // Имитация ожидания запроса
        Thread.sleep(mockConfig.getDelay());

        return switch (modelUri) {
            case "YandexGPT 5 Pro" -> mockGeneratedMessage.getFirst();
            case "DeepSeek V3" -> mockGeneratedMessage.get(1);
            case "ChatGPT 4o mini" -> mockGeneratedMessage.get(2);
            default -> throw new GlobalException("NO_EXISTED_MODEL", "Такой модели для генерации не существует");
        };
    }

    // Для фронта пока что достаточно без реализации векторов, они нужны только для хранения в бд, поэтому они позже
    @Override
    public ResponseGeneratedVectorDTO generateVector(String modelUri, String text) {
        return null;
    }

    // Контейнер для хранения сообщений, которые "сгенерированы" нейросетью
    public List<ResponseGeneratedMessageDTO> mockGeneratedMessage = new ArrayList<>();

    @PostConstruct
    private void initMockGeneratedMessage(){
        mockGeneratedMessage.add(new ResponseGeneratedMessageDTO(
                new ResponseGeneratedMessageDTO.MessageDTO(
                        "assistant",
                        "# Привет!"
                ),
                new ResponseGeneratedMessageDTO.UsageDTO(10, 5, 5, 0.001)
        ));

        mockGeneratedMessage.add(new ResponseGeneratedMessageDTO(
                new ResponseGeneratedMessageDTO.MessageDTO(
                        "assistant",
                        "## Информация\n\n- Привет!\n- Как дела?\n- Всё отлично!"
                ),
                new ResponseGeneratedMessageDTO.UsageDTO(25, 15, 10, 0.002)
        ));

        mockGeneratedMessage.add(new ResponseGeneratedMessageDTO(
                new ResponseGeneratedMessageDTO.MessageDTO(
                        "assistant",
                        "### Полное руководство\n\n**Добро пожаловать в наше руководство!**\n\nВ этом сообщении вы найдете:\n\n1. Введение\n2. Примеры использования\n3. Рекомендации по работе\n\n```java\n// Пример кода для вывода приветствия\nSystem.out.println(\"Hello, World!\");\n```\n\nДополнительная информация доступна на нашем [сайте](https://example.com)."
                ),
                new ResponseGeneratedMessageDTO.UsageDTO(50, 30, 20, 0.005)
        ));
    }
}
