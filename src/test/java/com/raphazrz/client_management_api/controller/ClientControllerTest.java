package com.raphazrz.client_management_api.controller;


import com.raphazrz.client_management_api.dto.request.ClientRequestDTO;
import com.raphazrz.client_management_api.dto.response.ClientResponseDTO;
import com.raphazrz.client_management_api.exception.DuplicateDocumentException;
import com.raphazrz.client_management_api.service.ClientService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;

import static com.raphazrz.client_management_api.util.TestDataFactory.createClientRequestDTO;
import static com.raphazrz.client_management_api.util.TestDataFactory.createClientResponseDTO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ClientController.class)
class ClientControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ClientService clientService;

    @Test
    @DisplayName("Should create a new client successfully.")
    void createClientCreated() throws Exception {
        // Arrange
        ClientRequestDTO request = createClientRequestDTO();
        ClientResponseDTO expectedResponse = createClientResponseDTO();

        when(clientService.createClient(request)).thenReturn(expectedResponse);

        // Act & Assert
        mockMvc.perform(
                        post("/clients")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value(expectedResponse.firstName()))
                .andExpect(jsonPath("$.lastName").value(expectedResponse.lastName()))
                .andExpect(jsonPath("$.document").value(expectedResponse.document()));

        verify(clientService).createClient(request);
    }

    @Test
    @DisplayName("Should return status 400 Bad Request.")
    void createClientBadRequest() throws Exception {
        // Arrange
        ClientRequestDTO request = new ClientRequestDTO(
                "",
                "Client",
                "123"
        );

        // Act & Assert
        mockMvc.perform(
                        post("/clients")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest());

        verify(clientService, never()).createClient(any());
    }

    @Test
    @DisplayName("Should return status 409 Conflict.")
    void createClientDuplicateDocumentException() throws Exception {
        // Arrange
        ClientRequestDTO request = createClientRequestDTO();
        when(clientService.createClient(request)).thenThrow(new DuplicateDocumentException());

        // Act & Assert
        mockMvc.perform(
                        post("/clients")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isConflict());

        verify(clientService).createClient(request);
    }
}