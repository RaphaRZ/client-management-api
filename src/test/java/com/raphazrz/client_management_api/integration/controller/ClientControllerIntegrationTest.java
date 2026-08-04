package com.raphazrz.client_management_api.integration.controller;

import com.raphazrz.client_management_api.dto.request.ClientRequestDTO;
import com.raphazrz.client_management_api.integration.base.BaseIntegrationTest;
import com.raphazrz.client_management_api.model.Client;
import com.raphazrz.client_management_api.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.raphazrz.client_management_api.util.TestDataFactory.createClientRequestDTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class ClientControllerIntegrationTest extends BaseIntegrationTest {
    private static final String BASE_URL = "/clients";

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
        mockMvc.perform(
                        post(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
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
        mockMvc.perform(
                        post(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
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


        mockMvc.perform(
                        post(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(firstRequest))
                )
                .andExpect(status().isCreated());


        // Arrange
        ClientRequestDTO secondRequest = new ClientRequestDTO(
                "Second",
                "Client",
                "00123456789"
        );

        // Act & Assert - HTTP response
        mockMvc.perform(
                        post(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(secondRequest))
                )
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
}
