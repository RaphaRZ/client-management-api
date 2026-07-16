package com.raphazrz.client_management_api.service;

import com.raphazrz.client_management_api.dto.request.ClientRequestDTO;
import com.raphazrz.client_management_api.dto.request.UpdateClientRequestDTO;
import com.raphazrz.client_management_api.dto.response.ClientContactResponseDTO;
import com.raphazrz.client_management_api.dto.response.ClientResponseDTO;
import com.raphazrz.client_management_api.exception.DuplicateDocumentException;
import com.raphazrz.client_management_api.mapper.ClientMapper;
import com.raphazrz.client_management_api.model.Client;
import com.raphazrz.client_management_api.repository.ClientRepository;
import jakarta.persistence.Table;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@RequiredArgsConstructor
@Service
public class ClientService { // CRIAR ClientQueryService
    private final ContactService contactService;
    private final ClientRepository clientRepository;
    private final ClientQueryService clientQueryService;

    @Transactional
    public ClientResponseDTO createClient(ClientRequestDTO request) {
        validateUniqueDocument(request.document());

        Client newClient = ClientMapper.toEntity(request);
        Client savedClient = saveClient(newClient);

        return ClientMapper.toResponseDTO(savedClient);
    }

    @Transactional(readOnly = true)
    public List<ClientResponseDTO> getClients() {
        return ClientMapper.toResponseDTO(clientQueryService.findAllClients());
    }

    @Transactional(readOnly = true)
    public ClientResponseDTO getClientById(Long id) {
        return ClientMapper.toResponseDTO(clientQueryService.findClientById(id));
    }

    @Transactional(readOnly = true)
    public List<ClientContactResponseDTO> getAllContactsByClientId(Long id) {
        return contactService.findAllContactsByClientId(id);
    }

    @Transactional
    public ClientResponseDTO updateClientById(Long id, UpdateClientRequestDTO request) {
        Client updatedContact = clientQueryService.findClientById(id);

        updatedContact.setFirstName(request.firstName());
        updatedContact.setLastName(request.lastName());
        updatedContact.setDocument(request.document());

        return ClientMapper.toResponseDTO(updatedContact);
    }

    public void validateUniqueDocument(String document) {
        if (clientRepository.existsByDocument(document))
            throw new DuplicateDocumentException("Document already registered.");
    }

    private Client saveClient(Client client) {
        return clientRepository.save(client);
    }
}
