package com.raphazrz.client_management_api.service;

import com.raphazrz.client_management_api.dto.request.ContactRequestDTO;
import com.raphazrz.client_management_api.dto.request.UpdateContactRequestDTO;
import com.raphazrz.client_management_api.dto.response.ContactResponseDTO;
import com.raphazrz.client_management_api.enumerator.ContactType;
import com.raphazrz.client_management_api.mapper.ContactMapper;
import com.raphazrz.client_management_api.model.Client;
import com.raphazrz.client_management_api.model.Contact;
import com.raphazrz.client_management_api.repository.ContactRepository;
import net.datafaker.Faker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.raphazrz.client_management_api.util.TestDataFactory.createClient;
import static com.raphazrz.client_management_api.util.TestDataFactory.createContactRequestDTO;
import static com.raphazrz.client_management_api.util.TestDataFactory.createUpdateContactRequestDTO;
import static com.raphazrz.client_management_api.util.TestDataFactory.createContact;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class ContactServiceTest {
    private static final Faker faker = new Faker();

    @Mock
    private ClientQueryService clientQueryService;

    @Mock
    private ContactRepository contactRepository;

    @InjectMocks
    private ContactService contactService;


    @Test
    @DisplayName("Should return a new contact as ContactResponseDTO successfully.")
    void createContactSuccess() {
        // Arrange
        ContactRequestDTO request = createContactRequestDTO();
        Client client = createClient();

        Contact savedContact = ContactMapper.toEntity(request);
        savedContact.setClient(client);

        ContactResponseDTO expectedResponse = ContactMapper.toResponseDTO(savedContact);

        when(clientQueryService.findClientById(request.clientId())).thenReturn(client);
        when(contactRepository.save(any(Contact.class))).thenReturn(savedContact);

        // Act
        ContactResponseDTO result = contactService.createContact(request);

        // Assert
        assertEquals(expectedResponse, result);
        verify(clientQueryService).findClientById(request.clientId());
        verify(contactRepository).save(any(Contact.class));
    }

    @Test
    @DisplayName("Should update the contact successfully.")
    void updateContactByIdSuccess() {
        // Arrange
        UpdateContactRequestDTO request = createUpdateContactRequestDTO();
        Contact updatedContact = createContact();

        when(contactRepository.findById(1L)).thenReturn(Optional.of(updatedContact));

        // Act
        ContactResponseDTO result = contactService.updateContactById(1L, request);

        // Assert
        assertEquals(ContactType.fromType(request.contactType()), updatedContact.getContactType());
        assertEquals(request.contact(), updatedContact.getContact());
        assertEquals(ContactMapper.toResponseDTO(updatedContact), result);
        verify(contactRepository).findById(1L);
    }
}
