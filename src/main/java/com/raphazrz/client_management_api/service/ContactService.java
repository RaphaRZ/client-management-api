package com.raphazrz.client_management_api.service;

import com.raphazrz.client_management_api.dto.request.ContactRequestDTO;
import com.raphazrz.client_management_api.dto.request.UpdateContactRequestDTO;
import com.raphazrz.client_management_api.dto.response.ClientContactResponseDTO;
import com.raphazrz.client_management_api.dto.response.ContactResponseDTO;
import com.raphazrz.client_management_api.enumerator.ContactType;
import com.raphazrz.client_management_api.exception.ContactNotFoundException;
import com.raphazrz.client_management_api.mapper.ContactMapper;
import com.raphazrz.client_management_api.model.Client;
import com.raphazrz.client_management_api.model.Contact;
import com.raphazrz.client_management_api.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@RequiredArgsConstructor
@Service
public class ContactService {
    private final ClientQueryService clientQueryService;
    private final ContactRepository contactRepository;

    @Transactional
    public ContactResponseDTO createContact(ContactRequestDTO request) {
        Client client = clientQueryService.findClientById(request.clientId());

        Contact newContact = ContactMapper.toEntity(request);
        newContact.setClient(client);

        Contact savedContact = saveContact(newContact);

        return ContactMapper.toResponseDTO(savedContact);
    }

    @Transactional
    public ContactResponseDTO updateContactById(Long id, UpdateContactRequestDTO request) {
        Contact updatedContact = findContactById(id);

        updatedContact.setContactType(ContactType.fromType(request.contactType()));
        updatedContact.setContact(request.contact());

        return ContactMapper.toResponseDTO(updatedContact);
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

    public List<ClientContactResponseDTO> findAllContactsByClientId(Long id) {
        Client client = clientQueryService.findClientById(id);
        List<Contact> contacts = client.getContacts();

        return contacts.stream()
                .map(ContactMapper::toClientContactResponseDTO)
                .toList();
    }

    private Contact saveContact(Contact contact) {
        return contactRepository.save(contact);
    }
}
