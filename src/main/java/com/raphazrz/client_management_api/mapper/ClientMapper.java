package com.raphazrz.client_management_api.mapper;

import com.raphazrz.client_management_api.dto.response.ClientContactResponseDTO;
import com.raphazrz.client_management_api.dto.request.ClientRequestDTO;
import com.raphazrz.client_management_api.dto.response.ClientResponseDTO;
import com.raphazrz.client_management_api.model.Client;
import com.raphazrz.client_management_api.model.Contact;

import java.util.List;


public class ClientMapper {
    public static ClientResponseDTO toResponseDTO(Client client) {
        List<ClientContactResponseDTO> contacts = null;

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

    private static List<ClientContactResponseDTO> toClientContactDTO(List<Contact> contacts) {
        return contacts.stream()
                .map(contact -> new ClientContactResponseDTO(
                        contact.getContactType(),
                        contact.getContact()))
                .toList();
    }
}
