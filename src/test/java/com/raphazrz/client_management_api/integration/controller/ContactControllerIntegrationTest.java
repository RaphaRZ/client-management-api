package com.raphazrz.client_management_api.integration.controller;

import com.raphazrz.client_management_api.dto.request.ClientRequestDTO;
import com.raphazrz.client_management_api.dto.request.ContactRequestDTO;
import com.raphazrz.client_management_api.enumerator.ContactType;
import com.raphazrz.client_management_api.integration.base.BaseIntegrationTest;
import com.raphazrz.client_management_api.model.Client;
import com.raphazrz.client_management_api.model.Contact;
import com.raphazrz.client_management_api.repository.ClientRepository;
import com.raphazrz.client_management_api.repository.ContactRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.raphazrz.client_management_api.enumerator.ContactType.fromType;
import static com.raphazrz.client_management_api.util.TestDataFactory.createClientRequestDTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class ContactControllerIntegrationTest extends BaseIntegrationTest {
    @Autowired
    ContactRepository contactRepository;

    @Autowired
    ClientRepository clientRepository;

    @BeforeEach
    void setUp() {
        clientRepository.deleteAll();
    }


    @Test
    @DisplayName("Should create a contact and persist it into the database.")
    void createContactCreated() throws Exception {
        // Arrange - Persist client
        createClientViaApi(createClientRequestDTO());
        Client persistedClient = clientRepository.findAll().getFirst();

        // Arrange
        ContactRequestDTO request = new ContactRequestDTO(
                ContactType.PHONE.getType(),
                "41000000001",
                persistedClient.getId()
        );

        // Act & Assert - HTTP response
        performPostContact(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.contactType").value(fromType(request.contactType()).name()))
                .andExpect(jsonPath("$.contact").value(request.contact()))
                .andExpect(jsonPath("$.clientId").value(request.clientId()));

        // Assert - Database state
        List<Contact> contacts = contactRepository.findAll();
        assertEquals(1, contacts.size());

        Contact persistedContact = contacts.getFirst();
        assertEquals(fromType(request.contactType()), persistedContact.getContactType());
        assertEquals(request.contact(), persistedContact.getContact());
        assertEquals(persistedClient.getId(), persistedContact.getClientId());
    }


}
