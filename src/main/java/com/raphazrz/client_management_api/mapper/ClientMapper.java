package com.raphazrz.client_management_api.mapper;

import com.raphazrz.client_management_api.dto.request.ClientRequestDTO;
import com.raphazrz.client_management_api.dto.response.ClientResponseDTO;
import com.raphazrz.client_management_api.model.Client;

import java.util.Collections;

public class ClientMapper {
    public static ClientResponseDTO toResponseDTO(Client client) {
        return new ClientResponseDTO(
                client.getFirstName(),
                client.getLastName(),
                client.getDocument(),
                client.getContacts());
    }

    public static Client toEntity(ClientRequestDTO clientRequestDTO) {
        return new Client(
                clientRequestDTO.firstName(),
                clientRequestDTO.lastName(),
                clientRequestDTO.document(),
                clientRequestDTO.contacts()
        );
    }
}
