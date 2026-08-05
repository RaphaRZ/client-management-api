package com.raphazrz.client_management_api.integration.controller;

import com.raphazrz.client_management_api.dto.request.ContactRequestDTO;
import com.raphazrz.client_management_api.dto.request.UpdateContactRequestDTO;
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

import java.util.List;

import static com.raphazrz.client_management_api.enumerator.ContactType.fromType;
import static com.raphazrz.client_management_api.util.TestDataFactory.createClientRequestDTO;
import static com.raphazrz.client_management_api.util.TestDataFactory.createUpdateContactRequestDTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

        // Arrange - Request
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

    @Test
    @DisplayName("Should return status 400 Bad Request when creating a contact with invalid request data.")
    void createContactBadRequest() throws Exception {
        // Arrange - Persist client
        createClientViaApi(createClientRequestDTO());
        Client persistedClient = clientRepository.findAll().getFirst();

        // Arrange - Request
        ContactRequestDTO request = new ContactRequestDTO(
                ContactType.PHONE.getType(),
                "",
                persistedClient.getId()
        );

        // Act & Assert - HTTP response
        performPostContact(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed."))
                .andExpect(jsonPath("$.validationErrors.contact").exists())
                .andExpect(jsonPath("$.statusCode").value(400));

        // Assert - Database state
        assertTrue(contactRepository.findAll().isEmpty());
    }

    @Test
    @DisplayName("Should return status 404 Not Found when creating a contact for a non-existent client.")
    void createContactClientNotFound() throws Exception {
        // Arrange
        ContactRequestDTO request = new ContactRequestDTO(
                ContactType.PHONE.getType(),
                "41999999999",
                1L
        );

        // Act & Assert - HTTP response
        performPostContact(request)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Client not found."))
                .andExpect(jsonPath("$.validationErrors").isEmpty())
                .andExpect(jsonPath("$.statusCode").value(404));

        // Assert - Database state
        assertTrue(contactRepository.findAll().isEmpty());
    }

    @Test
    @DisplayName("Should return status 200 OK and update a persisted contact by ID.")
    void updateContactByIdOk() throws Exception {
        // Arrange - Persist client
        createClientViaApi(createClientRequestDTO());
        Client persistedClient = clientRepository.findAll().getFirst();

        // Arrange - Persist contact
        createContactViaApi(new ContactRequestDTO
                (
                        ContactType.PHONE.getType(),
                        "4100000001",
                        persistedClient.getId()
                )
        );
        Contact persistedContact = contactRepository.findAll().getFirst();

        // Arrange - Update contact data
        UpdateContactRequestDTO request = createUpdateContactRequestDTO();

        // Act & Assert - HTTP response
        performPutContactById(persistedContact.getId(), request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(persistedContact.getId()))
                .andExpect(jsonPath("$.contactType").value(fromType(request.contactType()).name()))
                .andExpect(jsonPath("$.contact").value(request.contact()))
                .andExpect(jsonPath("$.clientId").value(persistedClient.getId()));

        // Assert - Database state
        assertEquals(1, contactRepository.count());

        Contact updatedContact = contactRepository.findById(persistedContact.getId()).orElseThrow();
        assertEquals(fromType(request.contactType()), updatedContact.getContactType());
        assertEquals(request.contact(), updatedContact.getContact());
        assertEquals(persistedClient.getId(), updatedContact.getClientId());
    }

    @Test
    @DisplayName("Should return status 400 Bad Request when updating a contact with invalid request data.")
    void updateContactByIdBadRequest() throws Exception {
        // Arrange - Persist client
        createClientViaApi(createClientRequestDTO());
        Client persistedClient = clientRepository.findAll().getFirst();

        // Arrange - Persist contact
        createContactViaApi(
                new ContactRequestDTO(
                        ContactType.PHONE.getType(),
                        "41000000001",
                        persistedClient.getId()
                )
        );
        Contact persistedContact = contactRepository.findAll().getFirst();

        // Arrange - Update contact data
        UpdateContactRequestDTO request = new UpdateContactRequestDTO(
                ContactType.PHONE.getType(),
                ""
        );

        // Act & Assert - HTTP response
        performPutContactById(persistedContact.getId(), request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed."))
                .andExpect(jsonPath("$.validationErrors.contact").exists())
                .andExpect(jsonPath("$.statusCode").value(400));

        // Assert - Database state
        Contact persistedContactAfterUpdate = contactRepository.findById(persistedContact.getId()).orElseThrow();

        assertEquals(ContactType.PHONE, persistedContactAfterUpdate.getContactType());
        assertEquals("41000000001", persistedContactAfterUpdate.getContact());
        assertEquals(persistedClient.getId(), persistedContactAfterUpdate.getClientId());
    }
}
