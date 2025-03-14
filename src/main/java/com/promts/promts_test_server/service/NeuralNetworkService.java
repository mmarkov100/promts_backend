package com.promts.promts_test_server.service;

import com.promts.promts_test_server.dto.NeuralNetwork.outbound.NeuralNetworkDTO;
import com.promts.promts_test_server.exception.GlobalException;
import com.promts.promts_test_server.repository.NeuralNetwork.NeuralNetworkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NeuralNetworkService {

    @Autowired
    private NeuralNetworkRepository networkRepository;

    public List<NeuralNetworkDTO> getAllNeuros(Long id, String uidFirebase){



        try {
            return networkRepository.getAllNeuros(uidFirebase, id);
        } catch (Exception e){
            throw new GlobalException("SERVICE_IS_NOT_ACTIVE", "Сервис недоступен");
        }
    }
}
