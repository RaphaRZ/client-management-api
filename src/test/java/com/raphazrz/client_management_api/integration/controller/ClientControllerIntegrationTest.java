package com.raphazrz.client_management_api.integration.controller;

import com.raphazrz.client_management_api.dto.request.ClientRequestDTO;
import com.raphazrz.client_management_api.dto.request.ContactRequestDTO;
import com.raphazrz.client_management_api.enumerator.ContactType;
import com.raphazrz.client_management_api.integration.base.BaseIntegrationTest;
import com.raphazrz.client_management_api.model.Client;
import com.raphazrz.client_management_api.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.raphazrz.client_management_api.util.TestDataFactory.createClientRequestDTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class ClientControllerIntegrationTest extends BaseIntegrationTest {
    private static final String BASE_CLIENTS_URL = "/clients";
    private static final String BASE_CONTACTS_URL = "/contacts";

    @Autowired
    private ClientRepository clientRepository;

    @BeforeEach
    void setUp() {
        clientRepository.deleteAll();
    }


    @Transactional
    @Test
    @DisplayName("Should create a client and persist it into the database.")
    void shouldCreateClientSuccessfully() throws Exception {
        // Arrange
        ClientRequestDTO request = createClientRequestDTO();

        // Act & Assert - HTTP response
        performPostClient(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value(request.firstName()))
                .andExpect(jsonPath("$.lastName").value(request.lastName()))
                .andExpect(jsonPath("$.document").value(request.document()))
                .andExpect(jsonPath("$.contacts").isEmpty());

        // Assert - Database state
        List<Client> clients = clientRepository.findAll();
        assertEquals(1, clients.size());

        Client persistedClient = clients.getFirst();
        assertEquals(request.firstName(), persistedClient.getFirstName());
        assertEquals(request.lastName(), persistedClient.getLastName());
        assertEquals(request.document(), persistedClient.getDocument());
        assertTrue(persistedClient.getContacts().isEmpty());
    }

    @Transactional
    @Test
    @DisplayName("Should return status 400 Bad Request when creating a client with invalid request data.")
    void shouldReturnStatus400BadRequestWhenCreatingClientWithInvalidRequestData() throws Exception {
        // Arrange
        ClientRequestDTO request = new ClientRequestDTO(
                "",
                "Client",
                "00123456789"
        );

        // Act & Assert - HTTP response
        performPostClient(request)
                .andExpect(jsonPath("$.message").value("Validation failed."))
                .andExpect(jsonPath("$.validationErrors.firstName").exists())
                .andExpect(jsonPath("$.statusCode").value(400));

        // Assert - Database state
        List<Client> clients = clientRepository.findAll();
        assertTrue(clients.isEmpty());
    }

    @Transactional
    @Test
    @DisplayName("Should return status 409 Conflict when creating a client with an already registered document.")
    void shouldReturnStatus409ConflictWhenCreatingClientWithAlreadyRegisteredDocument() throws Exception {
        // Arrange - Persist an existing client
        ClientRequestDTO firstRequest = new ClientRequestDTO(
                "First",
                "Client",
                "00123456789"
        );

        performPostClient(firstRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.document").value(firstRequest.document()));


        // Arrange
        ClientRequestDTO secondRequest = new ClientRequestDTO(
                "Second",
                "Client",
                firstRequest.document()
        );

        // Act & Assert - HTTP response
        performPostClient(secondRequest)
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Document already registered."))
                .andExpect(jsonPath("$.validationErrors").isEmpty())
                .andExpect(jsonPath("$.statusCode").value(409));

        // Assert - Database state
        List<Client> clients = clientRepository.findAll();
        assertEquals(1, clients.size());

        Client persistedClient = clients.getFirst();
        assertEquals(firstRequest.firstName(), persistedClient.getFirstName());
        assertEquals(firstRequest.lastName(), persistedClient.getLastName());
        assertEquals(firstRequest.document(), persistedClient.getDocument());
        assertTrue(persistedClient.getContacts().isEmpty());
    }

    @Transactional
    @Test
    @DisplayName("Should return status 200 OK and retrieve all persisted clients.")
    void shouldReturnStatus200OkAndRetrieveAllPersistedClients() throws Exception {
        // Arrange - Persist first client
        ClientRequestDTO firstRequest = createClientRequestDTO();
        performPostClient(firstRequest)
                .andExpect(status().isCreated());

        // Arrange - Persist second client
        ClientRequestDTO secondRequest = createClientRequestDTO();
        performPostClient(secondRequest)
                .andExpect(status().isCreated());

        // Act & Assert - HTTP response
        performGetClients()
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].firstName").value(firstRequest.firstName()))
                .andExpect(jsonPath("$[0].lastName").value(firstRequest.lastName()))
                .andExpect(jsonPath("$[0].document").value(firstRequest.document()))
                .andExpect(jsonPath("$[0].contacts").isEmpty())
                .andExpect(jsonPath("$[1].firstName").value(secondRequest.firstName()))
                .andExpect(jsonPath("$[1].lastName").value(secondRequest.lastName()))
                .andExpect(jsonPath("$[1].document").value(secondRequest.document()))
                .andExpect(jsonPath("$[1].contacts").isEmpty());

        // Assert - Database state
        assertEquals(2, clientRepository.count());
    }

    @Transactional
    @Test
    @DisplayName("Should return status 200 OK and retrieve a persisted client by ID.")
    void shouldReturnStatus200OkAndRetrievePersistedClientById() throws Exception {
        // Arrange - Persist client
        ClientRequestDTO persistClientRequest = createClientRequestDTO();
        performPostClient(persistClientRequest)
                .andExpect(status().isCreated());

        Client persistedClient = clientRepository.findAll().getFirst();

        // Act & Assert - HTTP response
        performGetClientById(persistedClient.getId())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(persistClientRequest.firstName()))
                .andExpect(jsonPath("$.lastName").value(persistClientRequest.lastName()))
                .andExpect(jsonPath("$.document").value(persistClientRequest.document()))
                .andExpect(jsonPath("$.contacts").isEmpty());

        // Assert - Database state
        assertEquals(1, clientRepository.count());
        assertEquals(persistClientRequest.firstName(), persistedClient.getFirstName());
        assertEquals(persistClientRequest.lastName(), persistedClient.getLastName());
        assertEquals(persistClientRequest.document(), persistedClient.getDocument());
        assertTrue(persistedClient.getContacts().isEmpty());
    }

    @Transactional
    @Test
    @DisplayName("Should return status 404 Not Found when retrieving a client with a non-existent ID.")
    void shouldReturnStatus404NotFoundWhenRetrievingClientWithNonExistentId() throws Exception {
        // Arrange
        Long nonExistentId = 1L;

        // Act & Assert - HTTP response
        performGetClientById(nonExistentId)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Client not found."))
                .andExpect(jsonPath("$.validationErrors").isEmpty())
                .andExpect(jsonPath("$.statusCode").value(404));

        // Assert - Database state
        assertTrue(clientRepository.findAll().isEmpty());
    }

    @Transactional
    @Test
    @DisplayName("Should return status 200 OK and retrieve all contacts from a persisted client.")
    void shouldReturnStatus200OkAndRetrieveAllContactsFromPersistedClient() throws Exception {
        // Arrange - Persist client
        ClientRequestDTO clientRequest = createClientRequestDTO();
        performPostClient(clientRequest)
                .andExpect(status().isCreated());

        Client persistedClient = clientRepository.findAll().getFirst();

        // Arrange - Persist first contact
        ContactRequestDTO firstContactRequest = new ContactRequestDTO(
                ContactType.PHONE.getType(),
                "41999999999",
                persistedClient.getId()
        );

        performPostContact(firstContactRequest)
                .andExpect(status().isCreated());

        // Arrange - Persist second contact
        ContactRequestDTO secondContactRequest = new ContactRequestDTO(
                ContactType.EMAIL.getType(),
                "john.doe@email.com",
                persistedClient.getId()
        );

        performPostContact(secondContactRequest)
                .andExpect(status().isCreated());

        // Act & Assert - HTTP response
        performGetAllContactsByClientId(persistedClient.getId())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].contactType").value(firstContactRequest.contactType()))
                .andExpect(jsonPath("$[0].contact").value(firstContactRequest.contact()))
                .andExpect(jsonPath("$[1].contactType").value(secondContactRequest.contactType()))
                .andExpect(jsonPath("$[1].contact").value(secondContactRequest.contact()));

        // Assert - Database state
        assertEquals(2, contactRepository.count());
    }

    private ResultActions performPostClient(ClientRequestDTO request) throws Exception {
        return mockMvc.perform(
                post(BASE_CLIENTS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );
    }

    private ResultActions performPostContact(ContactRequestDTO request) throws Exception {
        return mockMvc.perform(
                post(BASE_CONTACTS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));
    }

    private ResultActions performGetClients() throws Exception {
        return mockMvc.perform(get(BASE_CLIENTS_URL));
    }

    private ResultActions performGetClientById(Long id) throws Exception {
        return mockMvc.perform(get(BASE_CLIENTS_URL + "/{id}", id));
    }

    private ResultActions performGetAllContactsByClientId(Long id) throws Exception {
        return mockMvc.perform(get(BASE_CONTACTS_URL + "/{id}/contacts", id));
    }
}
