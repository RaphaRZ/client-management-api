package com.raphazrz.client_management_api.service;

import com.raphazrz.client_management_api.dto.request.ContactRequestDTO;
import com.raphazrz.client_management_api.dto.response.ContactResponseDTO;
import com.raphazrz.client_management_api.mapper.ContactMapper;
import com.raphazrz.client_management_api.model.Client;
import com.raphazrz.client_management_api.model.Contact;
import com.raphazrz.client_management_api.repository.ContactRepository;
import com.raphazrz.client_management_api.util.TestDataFactory;
import net.datafaker.Faker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

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
        ContactRequestDTO request = TestDataFactory.createContactRequestDTO();
        Client client = TestDataFactory.createClient();

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
}
