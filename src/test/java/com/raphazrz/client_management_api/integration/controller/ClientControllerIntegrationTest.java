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
                "123"
        );

        // Act & Assert - HTTP response
        mockMvc.perform(
                        post(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(jsonPath("$.message").value("Validation failed."))
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.validationErrors.firstName").exists());

        // Assert - Database state
        List<Client> clients = clientRepository.findAll();
        assertTrue(clients.isEmpty());
    }
}
