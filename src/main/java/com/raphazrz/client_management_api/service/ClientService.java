package com.raphazrz.client_management_api.service;

import com.raphazrz.client_management_api.dto.ClientDTO;
import com.raphazrz.client_management_api.model.Client;
import org.springframework.stereotype.Service;


@Service
public class ClientService {
    public Client createClient(ClientDTO clientDTO) {
        Client newClient = Client.builder()
                .firstName(clientDTO.firstName())
                .lastName(clientDTO.lastName())
                .document(clientDTO.document())
                .contacts(clientDTO.contacts())
                .build();

        saveClient(newClient);

        return newClient;
    }
}
