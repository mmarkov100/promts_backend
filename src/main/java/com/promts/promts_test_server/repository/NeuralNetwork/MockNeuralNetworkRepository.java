package com.promts.promts_test_server.repository.NeuralNetwork;

import com.promts.promts_test_server.config.MockConfig;
import com.promts.promts_test_server.dto.NeuralNetwork.outbound.NeuralNetworkDTO;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@Profile("mock")
public class MockNeuralNetworkRepository implements NeuralNetworkRepository{

    @Autowired
    private MockConfig mockConfig;

    @Override
    public List<NeuralNetworkDTO> getAllNeuros(String uidFirebase, Long id) throws InterruptedException {

        // Имитация ожидания запроса
        Thread.sleep(mockConfig.getDelay());

        return mockNeuralNetworkDTOS;
    }

    public List<NeuralNetworkDTO> mockNeuralNetworkDTOS = new ArrayList<>();

    @PostConstruct
    private void initMockNeuralNetworkDTOS() {
        mockNeuralNetworkDTOS.add(new NeuralNetworkDTO(1L, "DeepSeek V3", "openrouter/deepseek-v3", "Мощная модель для генерации текста."));
        mockNeuralNetworkDTOS.add(new NeuralNetworkDTO(2L, "YandexGPT 5 Pro", "yandex/yangexgpt-5-pro", "Самая продвинутая русская модель"));
        mockNeuralNetworkDTOS.add(new NeuralNetworkDTO(3L, "ChatGPT 4o mini", "openrouter/chatgpt-4o-mini", "Дешевая нейросеть для повседневных задач"));
    }
}
