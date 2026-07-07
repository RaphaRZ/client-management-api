package com.raphazrz.client_management_api.service;

import com.raphazrz.client_management_api.dto.request.ClientRequestDTO;
import com.raphazrz.client_management_api.dto.response.ClientResponseDTO;
import com.raphazrz.client_management_api.exception.ClientNotFoundException;
import com.raphazrz.client_management_api.exception.DuplicateDocumentException;
import com.raphazrz.client_management_api.mapper.ClientMapper;
import com.raphazrz.client_management_api.model.Client;
import com.raphazrz.client_management_api.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class ClientService {
    private final ClientRepository clientRepository;

    @Transactional
    public ClientResponseDTO createClient(ClientRequestDTO clientRequestDTO) {
        validateUniqueDocument(clientRequestDTO.document());

        Client newClient = ClientMapper.toEntity(clientRequestDTO);
        Client savedClient = saveClient(newClient);

        return ClientMapper.toResponseDTO(savedClient);
    }

    @Transactional(readOnly = true)
    public ClientResponseDTO getClientById(Long id) {
        return ClientMapper.toResponseDTO(findClientById(id));
    }

    public Client findClientById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(ClientNotFoundException::new);
    }

    public void validateUniqueDocument(String document) {
        if (clientRepository.existsByDocument(document))
            throw new DuplicateDocumentException("Document already registered.");
    }

    private Client saveClient(Client client) {
        return clientRepository.save(client);
    }
}
