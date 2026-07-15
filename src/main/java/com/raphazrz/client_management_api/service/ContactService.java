package com.raphazrz.client_management_api.service;

import com.raphazrz.client_management_api.dto.request.ContactRequestDTO;
import com.raphazrz.client_management_api.dto.response.ContactResponseDTO;
import com.raphazrz.client_management_api.exception.ClientNotFoundException;
import com.raphazrz.client_management_api.exception.ContactNotFoundException;
import com.raphazrz.client_management_api.mapper.ContactMapper;
import com.raphazrz.client_management_api.model.Client;
import com.raphazrz.client_management_api.model.Contact;
import com.raphazrz.client_management_api.repository.ClientRepository;
import com.raphazrz.client_management_api.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class ContactService {
    private final ClientService clientService;
    private final ContactRepository contactRepository;

    @Transactional
    public ContactResponseDTO createContact(ContactRequestDTO contactRequestDTO) {
        Client client = clientService.findClientById(contactRequestDTO.clientId());

        Contact newContact = ContactMapper.toEntity(contactRequestDTO);
        newContact.setClient(client);

        Contact savedContact = saveContact(newContact);

        return ContactMapper.toResponseDTO(savedContact);
    }

    @Transactional(readOnly = true)
    public ContactResponseDTO getContactById(Long id) {
        return ContactMapper.toResponseDTO(findContactById(id));
    }

    @Transactional
    public void deleteContact(Long contactId) {
        Contact contact = findContactById(contactId);

        contactRepository.delete(contact);
    }

    public Contact findContactById(Long contactId) {
        return contactRepository.findById(contactId)
                .orElseThrow(ContactNotFoundException::new);
    }

    private Contact saveContact(Contact contact){
        return contactRepository.save(contact);
    }
}
