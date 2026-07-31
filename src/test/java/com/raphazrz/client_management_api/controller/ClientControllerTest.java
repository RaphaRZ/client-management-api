package com.raphazrz.client_management_api.controller;


import com.raphazrz.client_management_api.dto.request.ClientRequestDTO;
import com.raphazrz.client_management_api.dto.response.ClientContactResponseDTO;
import com.raphazrz.client_management_api.dto.response.ClientResponseDTO;
import com.raphazrz.client_management_api.exception.ClientNotFoundException;
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

import java.util.List;

import static com.raphazrz.client_management_api.util.TestDataFactory.createClientRequestDTO;
import static com.raphazrz.client_management_api.util.TestDataFactory.createClientResponseDTO;
import static com.raphazrz.client_management_api.util.TestDataFactory.createClientContactResponseDTO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    @DisplayName("Should return status 201 Created.")
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

    @Test
    @DisplayName("Should return status 200 Ok.")
    void getClientsOk() throws Exception {
        // Arrange
        List<ClientResponseDTO> expectedResponse = List.of(
                createClientResponseDTO(),
                createClientResponseDTO()
        );

        when(clientService.getClients()).thenReturn(expectedResponse);

        // Act & Assert
        mockMvc.perform(get("/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(expectedResponse.size()))
                .andExpect(jsonPath("$[0].firstName").value(expectedResponse.getFirst().firstName()))
                .andExpect(jsonPath("$[0].lastName").value(expectedResponse.getFirst().lastName()))
                .andExpect(jsonPath("$[0].document").value(expectedResponse.getFirst().document()))
                .andExpect(jsonPath("$[1].firstName").value(expectedResponse.get(1).firstName()))
                .andExpect(jsonPath("$[1].lastName").value(expectedResponse.get(1).lastName()))
                .andExpect(jsonPath("$[1].document").value(expectedResponse.get(1).document()));

        verify(clientService).getClients();
    }

    @Test
    @DisplayName("Should return status 200 Ok.")
    void getClientByIdOk() throws Exception {
        // Arrange
        Long id = 1L;
        ClientResponseDTO expectedResponse = createClientResponseDTO();

        when(clientService.getClientById(id)).thenReturn(expectedResponse);

        // Act & Assert
        mockMvc.perform(get("/clients/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(expectedResponse.firstName()))
                .andExpect(jsonPath("$.lastName").value(expectedResponse.lastName()))
                .andExpect(jsonPath("$.document").value(expectedResponse.document()));

        verify(clientService).getClientById(id);
    }

    @Test
    @DisplayName("Should return status 404 Not Found.")
    void getClientByIdClientNotFoundException() throws Exception {
        // Arrange
        Long id = 1L;
        when(clientService.getClientById(id)).thenThrow(new ClientNotFoundException());

        // Act & Assert
        mockMvc.perform(get("/clients/{id}", id))
                .andExpect(status().isNotFound());

        verify(clientService).getClientById(id);
    }

    @Test
    @DisplayName("Should return status 200 Ok.")
    void getAllContactsByClientIdOk() throws Exception {
        // Arrange
        Long id = 1L;
        List<ClientContactResponseDTO> expectedResponse = List.of(
                createClientContactResponseDTO(),
                createClientContactResponseDTO()
        );

        when(clientService.getAllContactsByClientId(id)).thenReturn(expectedResponse);

        // Act & Assert
        mockMvc.perform(get("/clients/{id}/contacts", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(expectedResponse.size()))
                .andExpect(jsonPath("$[0].contactType").value(expectedResponse.getFirst().contactType().name()))
                .andExpect(jsonPath("$[0].contact").value(expectedResponse.getFirst().contact()))
                .andExpect(jsonPath("$[1].contactType").value(expectedResponse.get(1).contactType().name()))
                .andExpect(jsonPath("$[1].contact").value(expectedResponse.get(1).contact()));

        verify(clientService).getAllContactsByClientId(id);
    }

    @Test
    @DisplayName("Should return status 404 Not Found.")
    void getAllContactsByClientIdClientNotFoundException() throws Exception {
        // Arrange
        Long id = 1L;

        when(clientService.getAllContactsByClientId(id)).thenThrow(new ClientNotFoundException());

        // Act & Assert
        mockMvc.perform(get("/clients/{id}/contacts", id))
                .andExpect(status().isNotFound());

        verify(clientService).getAllContactsByClientId(id);
    }
}