package com.raphazrz.client_management_api.mapper;

import com.raphazrz.client_management_api.dto.other.ClientContactDTO;
import com.raphazrz.client_management_api.dto.request.ClientRequestDTO;
import com.raphazrz.client_management_api.dto.response.ClientResponseDTO;
import com.raphazrz.client_management_api.model.Client;
import com.raphazrz.client_management_api.model.Contact;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ClientMapper {
    public static ClientResponseDTO toResponseDTO(Client client) {
        List<ClientContactDTO> contacts = null;

        if (client.getContacts() != null)
            contacts = toClientContactDTO(client.getContacts());

        return new ClientResponseDTO(
                client.getFirstName(),
                client.getLastName(),
                client.getDocument(),
                contacts);
    }

    public static List<ClientResponseDTO> toResponseDTO(List<Client> clients) {
        return clients.stream()
                .map(ClientMapper::toResponseDTO)
                .toList();
    }

    public static Client toEntity(ClientRequestDTO clientRequestDTO) {
        return new Client(
                clientRequestDTO.firstName(),
                clientRequestDTO.lastName(),
                clientRequestDTO.document(),
                clientRequestDTO.contacts()
        );
    }

    private static List<ClientContactDTO> toClientContactDTO(List<Contact> contacts) {
        return contacts.stream()
                .map(contact -> new ClientContactDTO(
                        contact.getContactType(),
                        contact.getContact()))
                .toList();
    }
}
