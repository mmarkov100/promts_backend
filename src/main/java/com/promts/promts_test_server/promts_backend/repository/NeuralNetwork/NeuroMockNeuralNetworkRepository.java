package com.promts.promts_test_server.promts_backend.repository.NeuralNetwork;

import com.promts.promts_test_server.promts_backend.dto.NeuralNetwork.outbound.NeuralNetworkDTO;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@Profile({"neuro"})
public class NeuroMockNeuralNetworkRepository implements  NeuralNetworkRepository{
    @Override
    public List<NeuralNetworkDTO> getAllNeuros(String uidFirebase) throws InterruptedException {

        return mockNeuralNetworkDTOS;
    }

    public List<NeuralNetworkDTO> mockNeuralNetworkDTOS = new ArrayList<>();

    @PostConstruct
    private void initMockNeuralNetworkDTOS() {
        mockNeuralNetworkDTOS.add(new NeuralNetworkDTO(0L, "ChatGPT 4o mini", "openai/gpt-4o-mini", "Дешевая нейросеть для повседневных задач"));
        mockNeuralNetworkDTOS.add(new NeuralNetworkDTO(1L, "ChatGPT o4 mini high", "openai/o4-mini-high", "Продвинутая в логике модель"));
    }
}
