package com.raphazrz.client_management_api.service;

import com.raphazrz.client_management_api.dto.request.ClientRequestDTO;
import com.raphazrz.client_management_api.dto.request.UpdateClientRequestDTO;
import com.raphazrz.client_management_api.dto.response.ClientContactResponseDTO;
import com.raphazrz.client_management_api.dto.response.ClientResponseDTO;
import com.raphazrz.client_management_api.exception.DuplicateDocumentException;
import com.raphazrz.client_management_api.mapper.ClientMapper;
import com.raphazrz.client_management_api.model.Client;
import com.raphazrz.client_management_api.repository.ClientRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static com.raphazrz.client_management_api.util.TestDataFactory.createClient;
import static com.raphazrz.client_management_api.util.TestDataFactory.createClientRequestDTO;
import static com.raphazrz.client_management_api.util.TestDataFactory.createClientContactResponseDTO;
import static com.raphazrz.client_management_api.util.TestDataFactory.createUpdateClientRequestDTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;


@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {
    @Mock
    private ContactService contactService;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientQueryService clientQueryService;

    @InjectMocks
    private ClientService clientService;


    @Test
    @DisplayName("Should return a new client as ClientResponseDTO successfully.")
    void createClientSuccess() {
        // Arrange
        ClientRequestDTO request = createClientRequestDTO();
        Client savedClient = new Client(request.firstName(), request.lastName(), request.document(), new ArrayList<>());
        ClientResponseDTO expectedResponse = new ClientResponseDTO(request.firstName(), request.lastName(), request.document(), new ArrayList<>());

        when(clientRepository.existsByDocument(any(String.class))).thenReturn(false);
        when(clientRepository.save(any(Client.class))).thenReturn(savedClient);

        // Act
        ClientResponseDTO result = clientService.createClient(request);

        // Assert
        assertEquals(expectedResponse, result);
        verify(clientRepository).existsByDocument(request.document());
        verify(clientRepository).save(any(Client.class));
    }

    @Test
    @DisplayName("Should throw DuplicateDocumentException.")
    void createClientDuplicateDocumentException() {
        // Arrange
        ClientRequestDTO request = createClientRequestDTO();

        when(clientRepository.existsByDocument(request.document())).thenReturn(true);

        // Act
        DuplicateDocumentException thrown = assertThrows(
                DuplicateDocumentException.class,
                () -> clientService.createClient(request)
        );

        // Assert
        assertEquals("Document already registered.", thrown.getMessage());
        verify(clientRepository).existsByDocument(request.document());
        verify(clientRepository, never()).save(any(Client.class));
    }

    @Test
    @DisplayName("Should return all clients successfully.")
    void getClientsSuccess() {
        // Arrange
        List<Client> clients = List.of(createClient(), createClient());
        List<ClientResponseDTO> expectedResponse = clients.stream()
                .map(ClientMapper::toResponseDTO)
                .toList();

        when(clientQueryService.findAllClients()).thenReturn(clients);

        // Act
        List<ClientResponseDTO> result = clientService.getClients();

        // Assert
        assertEquals(expectedResponse, result);
        verify(clientQueryService).findAllClients();
    }

    @Test
    @DisplayName("Should return the client successfully.")
    void getClientByIdSuccess() {
        // Arrange
        Client client = createClient();
        ClientResponseDTO expectedResponse = ClientMapper.toResponseDTO(client);

        when(clientQueryService.findClientById(1L)).thenReturn(client);

        // Act
        ClientResponseDTO result = clientService.getClientById(1L);

        // Assert
        assertEquals(expectedResponse, result);
        verify(clientQueryService).findClientById(1L);
    }

    @Test
    @DisplayName("Should return all client contacts successfully.")
    void getAllContactsByClientIdSuccess() {
        // Arrange
        List<ClientContactResponseDTO> expectedResponse = List.of(createClientContactResponseDTO(), createClientContactResponseDTO());

        when(contactService.findAllContactsByClientId(1L)).thenReturn(expectedResponse);

        // Act
        List<ClientContactResponseDTO> result = clientService.getAllContactsByClientId(1L);

        // Assert
        assertEquals(expectedResponse, result);
        verify(contactService).findAllContactsByClientId(1L);
    }

    @Test
    @DisplayName("Should update the client successfully.")
    void updateClientByIdSuccess() {
        // Arrange
        UpdateClientRequestDTO request = createUpdateClientRequestDTO();
        Client updatedClient = createClient();

        when(clientQueryService.findClientById(1L)).thenReturn(updatedClient);
        when(clientRepository.existsByDocumentAndIdNot(request.document(), 1L)).thenReturn(false);

        // Act
        ClientResponseDTO result = clientService.updateClientById(1L, request);

        // Assert
        assertEquals(request.firstName(), updatedClient.getFirstName());
        assertEquals(request.lastName(), updatedClient.getLastName());
        assertEquals(request.document(), updatedClient.getDocument());
        assertEquals(ClientMapper.toResponseDTO(updatedClient), result);
        verify(clientQueryService).findClientById(1L);
        verify(clientRepository).existsByDocumentAndIdNot(request.document(), 1L);
    }

    @Test
    @DisplayName("Should throw DuplicateDocumentException.")
    void updateClientByIdDuplicateDocumentException() {
        // Arrange
        UpdateClientRequestDTO request = createUpdateClientRequestDTO();
        Client client = createClient();

        when(clientQueryService.findClientById(1L)).thenReturn(client);
        when(clientRepository.existsByDocumentAndIdNot(request.document(), 1L)).thenReturn(true);

        // Act
        DuplicateDocumentException thrown = assertThrows(
                DuplicateDocumentException.class,
                () -> clientService.updateClientById(1L, request)
        );

        // Assert
        assertEquals("Document already registered.", thrown.getMessage());
        verify(clientQueryService).findClientById(1L);
        verify(clientRepository).existsByDocumentAndIdNot(request.document(), 1L);
    }

    @Test
    @DisplayName("Should delete the client successfully.")
    void deleteClientByIdSuccess() {
        // Arrange
        Client client = createClient();

        when(clientQueryService.findClientById(1L)).thenReturn(client);

        // Act
        clientService.deleteClientById(1L);

        // Assert
        verify(clientQueryService).findClientById(1L);
        verify(clientRepository).delete(client);
    }
}
