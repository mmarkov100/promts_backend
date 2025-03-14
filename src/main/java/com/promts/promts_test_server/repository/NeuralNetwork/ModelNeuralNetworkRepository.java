package com.promts.promts_test_server.repository.NeuralNetwork;

import com.promts.promts_test_server.dto.NeuralNetwork.outbound.NeuralNetworkDTO;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Profile("model")
public class ModelNeuralNetworkRepository implements NeuralNetworkRepository{
    @Override
    public List<NeuralNetworkDTO> getAllNeuros(String uidFirebase, Long id) {
        return List.of();
    }
}
