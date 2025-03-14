package com.promts.promts_test_server.repository.NeuralNetwork;

import com.promts.promts_test_server.dto.NeuralNetwork.outbound.NeuralNetworkDTO;

import java.util.List;

public interface NeuralNetworkRepository {
    List<NeuralNetworkDTO> getAllNeuros(String uidFirebase, Long id) throws InterruptedException;
}
