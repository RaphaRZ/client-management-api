package com.raphazrz.client_management_api.controller;


import com.raphazrz.client_management_api.dto.request.ClientRequestDTO;
import com.raphazrz.client_management_api.dto.request.UpdateClientRequestDTO;
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
import static com.raphazrz.client_management_api.util.TestDataFactory.createUpdateClientRequestDTO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ClientController.class)
class ClientControllerTest {
    private static final String BASE_URL = "/clients";

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
                        post(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value(expectedResponse.firstName()))
                .andExpect(jsonPath("$.lastName").value(expectedResponse.lastName()))
                .andExpect(jsonPath("$.document").value(expectedResponse.document()))
                .andExpect(jsonPath("$.contacts.length()").value(expectedResponse.contacts().size()));

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
                        post(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest());

        verify(clientService, never()).createClient(any(ClientRequestDTO.class));
    }

    @Test
    @DisplayName("Should return status 409 Conflict.")
    void createClientDuplicateDocument() throws Exception {
        // Arrange
        ClientRequestDTO request = createClientRequestDTO();
        when(clientService.createClient(request)).thenThrow(new DuplicateDocumentException());

        // Act & Assert
        mockMvc.perform(
                        post(BASE_URL)
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
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(expectedResponse.size()))
                .andExpect(jsonPath("$[0].firstName").value(expectedResponse.getFirst().firstName()))
                .andExpect(jsonPath("$[0].lastName").value(expectedResponse.getFirst().lastName()))
                .andExpect(jsonPath("$[0].document").value(expectedResponse.getFirst().document()))
                .andExpect(jsonPath("$[0]..contacts.length()").value(expectedResponse.getFirst().contacts().size()))
                .andExpect(jsonPath("$[1].firstName").value(expectedResponse.get(1).firstName()))
                .andExpect(jsonPath("$[1].lastName").value(expectedResponse.get(1).lastName()))
                .andExpect(jsonPath("$[1].document").value(expectedResponse.get(1).document()))
                .andExpect(jsonPath("$[1].contacts.length()").value(expectedResponse.getFirst().contacts().size()));

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
        mockMvc.perform(get(BASE_URL + "/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(expectedResponse.firstName()))
                .andExpect(jsonPath("$.lastName").value(expectedResponse.lastName()))
                .andExpect(jsonPath("$.document").value(expectedResponse.document()));

        verify(clientService).getClientById(id);
    }

    @Test
    @DisplayName("Should return status 404 Not Found.")
    void getClientByIdClientNotFound() throws Exception {
        // Arrange
        Long id = 1L;
        when(clientService.getClientById(id)).thenThrow(new ClientNotFoundException());

        // Act & Assert
        mockMvc.perform(get(BASE_URL + "/{id}", id))
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
        mockMvc.perform(get(BASE_URL + "/{id}/contacts", id))
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
    void getAllContactsByClientIdClientNotFound() throws Exception {
        // Arrange
        Long id = 1L;

        when(clientService.getAllContactsByClientId(id)).thenThrow(new ClientNotFoundException());

        // Act & Assert
        mockMvc.perform(get(BASE_URL + "/{id}/contacts", id))
                .andExpect(status().isNotFound());

        verify(clientService).getAllContactsByClientId(id);
    }

    @Test
    @DisplayName("Should return status 200 Ok.")
    void updateClientByIdOk() throws Exception {
        // Arrange
        Long id = 1L;
        UpdateClientRequestDTO request = createUpdateClientRequestDTO();
        ClientResponseDTO expectedResponse = createClientResponseDTO();

        when(clientService.updateClientById(id, request)).thenReturn(expectedResponse);

        // Act & Assert
        mockMvc.perform(put(BASE_URL + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(expectedResponse.firstName()))
                .andExpect(jsonPath("$.lastName").value(expectedResponse.lastName()))
                .andExpect(jsonPath("$.document").value(expectedResponse.document()))
                .andExpect(jsonPath("$.contacts.length()").value(expectedResponse.contacts().size()));

        verify(clientService).updateClientById(id, request);
    }

    @Test
    @DisplayName("Should return status 400 Bad Request.")
    void updateClientByIdBadRequest() throws Exception {
        // Arrange
        Long id = 1L;
        UpdateClientRequestDTO request = new UpdateClientRequestDTO(
                "",
                "Client",
                "123"
        );

        // Act & Assert
        mockMvc.perform(
                        put(BASE_URL + "/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest());

        verify(clientService, never()).updateClientById(any(Long.class), any(UpdateClientRequestDTO.class));
    }

    @Test
    @DisplayName("Should return status 409 Conflict.")
    void updateClientByIdDuplicateDocument() throws Exception {
        // Arrange
        Long id = 1L;
        UpdateClientRequestDTO request = createUpdateClientRequestDTO();

        when(clientService.updateClientById(id, request))
                .thenThrow(new DuplicateDocumentException());

        // Act & Assert
        mockMvc.perform(
                        put(BASE_URL + "/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isConflict());

        verify(clientService).updateClientById(id, request);
    }

    @Test
    @DisplayName("Should return status 204 No Content.")
    void deleteClientByIdNoContent() throws Exception {
        // Arrange
        Long id = 1L;

        doNothing().when(clientService).deleteClientById(id);

        // Act & Assert
        mockMvc.perform(delete(BASE_URL + "/{id}", id))
                .andExpect(status().isNoContent());

        verify(clientService).deleteClientById(id);
    }

    @Test
    @DisplayName("Should return status 404 Not Found.")
    void deleteClientByIdNotFound() throws Exception {
        // Arrange
        Long id = 1L;

        doThrow(new ClientNotFoundException())
                .when(clientService)
                .deleteClientById(id);

        // Act & Assert
        mockMvc.perform(delete(BASE_URL + "/{id}", id))
                .andExpect(status().isNotFound());

        verify(clientService).deleteClientById(id);
    }
}