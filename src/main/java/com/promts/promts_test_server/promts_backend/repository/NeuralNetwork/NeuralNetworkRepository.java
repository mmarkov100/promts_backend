package com.promts.promts_test_server.promts_backend.repository.NeuralNetwork;

import com.promts.promts_test_server.promts_backend.dto.NeuralNetwork.outbound.NeuralNetworkDTO;

import java.util.List;

public interface NeuralNetworkRepository {
    List<NeuralNetworkDTO> getAllNeuros(String uidFirebase) throws InterruptedException;
}
