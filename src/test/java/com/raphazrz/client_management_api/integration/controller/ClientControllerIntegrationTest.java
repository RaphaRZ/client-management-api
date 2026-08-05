package com.raphazrz.client_management_api.integration.controller;

import com.raphazrz.client_management_api.dto.request.ClientRequestDTO;
import com.raphazrz.client_management_api.dto.request.ContactRequestDTO;
import com.raphazrz.client_management_api.dto.request.UpdateClientRequestDTO;
import com.raphazrz.client_management_api.enumerator.ContactType;
import com.raphazrz.client_management_api.integration.base.BaseIntegrationTest;
import com.raphazrz.client_management_api.model.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import static com.raphazrz.client_management_api.enumerator.ContactType.fromType;
import static com.raphazrz.client_management_api.util.TestDataFactory.createClientRequestDTO;
import static com.raphazrz.client_management_api.util.TestDataFactory.createUpdateClientRequestDTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class ClientControllerIntegrationTest extends BaseIntegrationTest {
    @BeforeEach
    void setUp() {
        clientRepository.deleteAll();
    }


    @Transactional
    @Test
    @DisplayName("Should create a client and persist it into the database.")
    void createClientCreated() throws Exception {
        // Arrange
        ClientRequestDTO request = createClientRequestDTO();

        // Act & Assert - HTTP response
        performPostClient(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.firstName").value(request.firstName()))
                .andExpect(jsonPath("$.lastName").value(request.lastName()))
                .andExpect(jsonPath("$.document").value(request.document()))
                .andExpect(jsonPath("$.contacts").isEmpty());

        // Assert - Database state
        assertEquals(1, clientRepository.count());

        Client persistedClient = clientRepository.findAll().getFirst();
        assertEquals(request.firstName(), persistedClient.getFirstName());
        assertEquals(request.lastName(), persistedClient.getLastName());
        assertEquals(request.document(), persistedClient.getDocument());
        assertTrue(persistedClient.getContacts().isEmpty());
    }

    @Test
    @DisplayName("Should return status 400 Bad Request when creating a client with invalid request data.")
    void createClientBadRequest() throws Exception {
        // Arrange
        ClientRequestDTO badRequest = new ClientRequestDTO(
                "",
                "Client",
                "00123456789"
        );

        // Act & Assert - HTTP response
        performPostClient(badRequest)
                .andExpect(jsonPath("$.message").value("Validation failed."))
                .andExpect(jsonPath("$.validationErrors.firstName").exists())
                .andExpect(jsonPath("$.statusCode").value(400));

        // Assert - Database state
        assertEquals(0, clientRepository.count());
    }

    @Transactional
    @Test
    @DisplayName("Should return status 409 Conflict when creating a client with an already registered document.")
    void createClientDuplicateDocumentException() throws Exception {
        // Arrange - Persist an existing client
        ClientRequestDTO firstClientRequest = createClientRequestDTO();
        Client persistedClient = createClientViaApi(firstClientRequest);

        // Arrange
        ClientRequestDTO clientRequest = new ClientRequestDTO(
                "Second",
                "Client",
                persistedClient.getDocument()
        );

        // Act & Assert - HTTP response
        performPostClient(clientRequest)
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Document already registered."))
                .andExpect(jsonPath("$.validationErrors").isEmpty())
                .andExpect(jsonPath("$.statusCode").value(409));

        // Assert - Database state
        assertEquals(1, clientRepository.count());
        assertEquals(firstClientRequest.firstName(), persistedClient.getFirstName());
        assertEquals(firstClientRequest.lastName(), persistedClient.getLastName());
        assertEquals(firstClientRequest.document(), persistedClient.getDocument());
        assertTrue(persistedClient.getContacts().isEmpty());
    }

    @Test
    @DisplayName("Should return status 200 OK and retrieve all persisted clients.")
    void getClientsOk() throws Exception {
        // Arrange - Persist first and second clients
        Client firstClient = createClientViaApi(createClientRequestDTO());
        Client secondClient = createClientViaApi(createClientRequestDTO());


        System.out.println("FIRST CLIENT ID >>>>" + firstClient.getId());
        System.out.println("SECOND CLIENT ID >>>>" + secondClient.getId());

        // Act & Assert - HTTP response
        performGetClients()
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(firstClient.getId()))
                .andExpect(jsonPath("$[0].firstName").value(firstClient.getFirstName()))
                .andExpect(jsonPath("$[0].lastName").value(firstClient.getLastName()))
                .andExpect(jsonPath("$[0].document").value(firstClient.getDocument()))
                .andExpect(jsonPath("$[0].contacts").isEmpty())
                .andExpect(jsonPath("$[1].id").value(secondClient.getId()))
                .andExpect(jsonPath("$[1].firstName").value(secondClient.getFirstName()))
                .andExpect(jsonPath("$[1].lastName").value(secondClient.getLastName()))
                .andExpect(jsonPath("$[1].document").value(secondClient.getDocument()))
                .andExpect(jsonPath("$[1].contacts").isEmpty());

        // Assert - Database state
        assertEquals(2, clientRepository.count());
    }

    @Transactional
    @Test
    @DisplayName("Should return status 200 OK and retrieve a persisted client by ID.")
    void getClientByIdOk() throws Exception {
        // Arrange - Persist client
        ClientRequestDTO request = createClientRequestDTO();
        Client persistedClient = createClientViaApi(request);

        // Act & Assert - HTTP response
        performGetClientById(persistedClient.getId())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(persistedClient.getId()))
                .andExpect(jsonPath("$.firstName").value(persistedClient.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(persistedClient.getLastName()))
                .andExpect(jsonPath("$.document").value(persistedClient.getDocument()))
                .andExpect(jsonPath("$.contacts").isEmpty());

        // Assert - Database state
        assertEquals(1, clientRepository.count());
        assertEquals(request.firstName(), persistedClient.getFirstName());
        assertEquals(request.lastName(), persistedClient.getLastName());
        assertEquals(request.document(), persistedClient.getDocument());
        assertTrue(persistedClient.getContacts().isEmpty());
    }

    @Test
    @DisplayName("Should return status 404 Not Found when retrieving a client with a non-existent ID.")
    void getClientByIdClientNotFoundException() throws Exception {
        // Arrange
        Long nonExistentId = 1L;

        // Act & Assert - HTTP response
        performGetClientById(nonExistentId)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Client not found."))
                .andExpect(jsonPath("$.validationErrors").isEmpty())
                .andExpect(jsonPath("$.statusCode").value(404));

        // Assert - Database state
        assertEquals(0, clientRepository.count());
    }

    @Test
    @DisplayName("Should return status 200 OK and retrieve all contacts from a persisted client.")
    void getAllContactsByClientIdOk() throws Exception {
        // Arrange - Persist client
        createClientViaApi(createClientRequestDTO());
        Client persistedClient = clientRepository.findAll().getFirst();

        // Arrange - Persist first and second contacts
        ContactRequestDTO firstContactRequest = createContactViaApi(new ContactRequestDTO(
                ContactType.PHONE.getType(),
                "41000000001",
                persistedClient.getId())
        );
        ContactRequestDTO secondContactRequest = createContactViaApi(new ContactRequestDTO(
                ContactType.EMAIL.getType(),
                "anyemail@gmail.com",
                persistedClient.getId())
        );

        // Act & Assert - HTTP response
        performGetAllContactsByClientId(persistedClient.getId())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").isNumber())
                .andExpect(jsonPath("$[0].contactType").value(fromType(firstContactRequest.contactType()).name()))
                .andExpect(jsonPath("$[0].contact").value(firstContactRequest.contact()))
                .andExpect(jsonPath("$[1].id").isNumber())
                .andExpect(jsonPath("$[1].contactType").value(fromType(secondContactRequest.contactType()).name()))
                .andExpect(jsonPath("$[1].contact").value(secondContactRequest.contact()));

        // Assert - Database state
        assertEquals(2, contactRepository.count());
    }

    @Test
    @DisplayName("Should return status 404 Not Found when retrieving contacts from a non-existent client.")
    void getAllContactsByClientIdClientNotFound() throws Exception {
        // Arrange
        Long nonExistentId = 1L;

        // Act & Assert - HTTP response
        performGetAllContactsByClientId(nonExistentId)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Client not found."))
                .andExpect(jsonPath("$.validationErrors").isEmpty())
                .andExpect(jsonPath("$.statusCode").value(404));

        // Assert - Database state
        assertEquals(0, contactRepository.count());
    }

    @Transactional
    @Test
    @DisplayName("Should return status 200 OK and update a persisted client by ID.")
    void updateClientByIdOk() throws Exception {
        // Arrange - Persist client
        Client persistedClient = createClientViaApi(createClientRequestDTO());

        // Arrange - Update Client request data
        UpdateClientRequestDTO request = createUpdateClientRequestDTO();

        // Act & Assert - HTTP response
        performPutClientById(persistedClient.getId(), request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(persistedClient.getId()))
                .andExpect(jsonPath("$.firstName").value(persistedClient.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(persistedClient.getLastName()))
                .andExpect(jsonPath("$.document").value(persistedClient.getDocument()));

        // Assert - Database state
        assertEquals(1, clientRepository.count());

        Client updatedClient = clientRepository.findById(persistedClient.getId()).orElseThrow();
        assertEquals(request.firstName(), updatedClient.getFirstName());
        assertEquals(request.lastName(), updatedClient.getLastName());
        assertEquals(request.document(), updatedClient.getDocument());
    }

    @Test
    @DisplayName("Should return status 400 Bad Request when updating a client with invalid request data.")
    void updateClientByIdBadRequest() throws Exception {
        // Arrange - Persist client
        Client persistedClient = createClientViaApi(createClientRequestDTO());

        // Arrange - Update Client data
        UpdateClientRequestDTO badRequest = new UpdateClientRequestDTO(
                "",
                "Client",
                "00123456789"
        );

        // Act & Assert - HTTP response
        performPutClientById(persistedClient.getId(), badRequest)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed."))
                .andExpect(jsonPath("$.validationErrors.firstName").exists())
                .andExpect(jsonPath("$.statusCode").value(400));

        // Assert - Database state
        assertEquals(1, clientRepository.count());

        Client persistedClientAfterUpdate = clientRepository.findAll().getFirst();
        assertEquals(persistedClient.getFirstName(), persistedClientAfterUpdate.getFirstName());
        assertEquals(persistedClient.getLastName(), persistedClientAfterUpdate.getLastName());
        assertEquals(persistedClient.getDocument(), persistedClientAfterUpdate.getDocument());
    }

    @Test
    @DisplayName("Should return status 409 Conflict when updating a client with an already registered document.")
    void updateClientByIdDuplicateDocument() throws Exception {
        // Arrange - Persist first and second clients
        Client firstPersistedClient = createClientViaApi(createClientRequestDTO());
        Client secondPersistedClient = createClientViaApi(createClientRequestDTO());

        // Arrange - Update Client data
        UpdateClientRequestDTO request = new UpdateClientRequestDTO(
                "Updated",
                "Client",
                firstPersistedClient.getDocument()
        );

        // Act & Assert - HTTP response
        performPutClientById(secondPersistedClient.getId(), request)
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Document already registered."))
                .andExpect(jsonPath("$.validationErrors").isEmpty())
                .andExpect(jsonPath("$.statusCode").value(409));

        // Assert - Database state
        assertEquals(2, clientRepository.count());

        Client secondClientAfterUpdate = clientRepository.findById(secondPersistedClient.getId()).orElseThrow();
        assertEquals(secondPersistedClient.getFirstName(), secondClientAfterUpdate.getFirstName());
        assertEquals(secondPersistedClient.getLastName(), secondClientAfterUpdate.getLastName());
        assertEquals(secondPersistedClient.getDocument(), secondClientAfterUpdate.getDocument());
    }

    @Test
    @DisplayName("Should return status 204 No Content when deleting a persisted client by ID.")
    void deleteClientByIdNoContent() throws Exception {
        // Arrange - Persist client
        Client persistedClient = createClientViaApi(createClientRequestDTO());

        // Act & Assert - HTTP response
        performDeleteClientById(persistedClient.getId())
                .andExpect(status().isNoContent());

        // Assert - Database state
        assertEquals(0, clientRepository.count());
    }

    @Test
    @DisplayName("Should return status 404 Not Found when deleting a client with a non-existent ID.")
    void deleteClientByIdNotFound() throws Exception {
        // Arrange
        Long nonExistentId = 1L;

        // Act & Assert - HTTP response
        performDeleteClientById(nonExistentId)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Client not found."))
                .andExpect(jsonPath("$.validationErrors").isEmpty())
                .andExpect(jsonPath("$.statusCode").value(404));

        // Assert - Database state
        assertEquals(0, clientRepository.count());
    }
}
