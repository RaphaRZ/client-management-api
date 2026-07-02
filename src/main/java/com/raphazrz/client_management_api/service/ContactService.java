package com.raphazrz.client_management_api.service;

import com.raphazrz.client_management_api.dto.request.ContactRequestDTO;
import com.raphazrz.client_management_api.dto.response.ContactResponseDTO;
import com.raphazrz.client_management_api.exception.ClientNotFoundException;
import com.raphazrz.client_management_api.mapper.ContactMapper;
import com.raphazrz.client_management_api.model.Client;
import com.raphazrz.client_management_api.model.Contact;
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
        Contact newContact = ContactMapper.toEntity(contactRequestDTO);
        addClientToContact(newContact);

        Contact savedContact = saveContact(newContact);

        return ContactMapper.toResponseDTO(savedContact);
    }

    private void addClientToContact(Contact contact) {
        Client client = clientRepository.findById(contact.getId())
                .orElseThrow(() -> new ClientNotFoundException("Client not found."));

        contact.setClient(client);
    }

    private Contact saveContact(Contact contact){
        return contactRepository.save(contact);
    }
}
