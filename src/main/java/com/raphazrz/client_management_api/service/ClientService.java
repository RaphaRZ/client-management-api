package com.raphazrz.client_management_api.service;

import com.raphazrz.client_management_api.dto.request.ClientRequestDTO;
import com.raphazrz.client_management_api.dto.response.ClientResponseDTO;
import com.raphazrz.client_management_api.mapper.ClientMapper;
import com.raphazrz.client_management_api.model.Client;
import com.raphazrz.client_management_api.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class ClientService {
    private final ClientRepository clientRepository;

    private Client saveClient(Client client) {
        return clientRepository.save(client);
    }

    public ClientResponseDTO createClient(ClientRequestDTO clientRequestDTO) {
        Client newClient = ClientMapper.toEntity(clientRequestDTO);

        Client savedClient = saveClient(newClient);

        return ClientMapper.toResponseDTO(savedClient);
    }
}
