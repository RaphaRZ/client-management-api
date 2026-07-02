package com.raphazrz.client_management_api.service;

import com.raphazrz.client_management_api.dto.request.ContactRequestDTO;
import com.raphazrz.client_management_api.dto.response.ClientResponseDTO;
import com.raphazrz.client_management_api.dto.response.ContactResponseDTO;
import com.raphazrz.client_management_api.exception.ClientNotFoundException;
import com.raphazrz.client_management_api.model.Client;
import com.raphazrz.client_management_api.repository.ClientRepository;
import com.raphazrz.client_management_api.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class ContactService {
    private final ClientRepository clientRepository;
    private final ContactRepository contactRepository;

    public ContactResponseDTO createContact(ContactRequestDTO contactRequestDTO) {
        validateClientExistence(contactRequestDTO.clientId());


    }


}
