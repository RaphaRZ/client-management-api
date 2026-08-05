package com.raphazrz.client_management_api.service;

import com.raphazrz.client_management_api.exception.ClientNotFoundException;
import com.raphazrz.client_management_api.model.Client;
import com.raphazrz.client_management_api.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@RequiredArgsConstructor
@Service
public class ClientQueryService {
    private final ClientRepository clientRepository;

    public List<Client> findAllClients() {
        return clientRepository.findAll(Sort.by("id"));
    }

    public Client findClientById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(ClientNotFoundException::new);
    }
}
