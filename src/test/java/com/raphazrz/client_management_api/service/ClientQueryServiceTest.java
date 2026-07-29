package com.raphazrz.client_management_api.service;

import com.raphazrz.client_management_api.exception.ClientNotFoundException;
import com.raphazrz.client_management_api.model.Client;
import com.raphazrz.client_management_api.repository.ClientRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.raphazrz.client_management_api.util.TestDataFactory.createClient;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class ClientQueryServiceTest {
    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientQueryService clientQueryService;


    @Test
    @DisplayName("Should return all clients successfully.")
    void findAllClientsSuccess() {
        // Arrange
        List<Client> clients = List.of(createClient(), createClient());
        when(clientRepository.findAll()).thenReturn(clients);

        // Act
        List<Client> result = clientQueryService.findAllClients();

        // Assert
        assertEquals(clients, result);
        verify(clientRepository).findAll();
    }

    @Test
    @DisplayName("Should return the client successfully.")
    void findClientByIdSuccess() {
        // Arrange
        Client client = createClient();
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        // Act
        Client result = clientQueryService.findClientById(1L);

        // Assert
        assertEquals(client, result);
        verify(clientRepository).findById(1L);
    }

    @Test
    @DisplayName("Should throw ClientNotFoundException.")
    void findClientByIdClientNotFoundException() {
        // Arrange
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        ClientNotFoundException thrown = assertThrows(
                ClientNotFoundException.class,
                () -> clientQueryService.findClientById(1L));

        // Assert
        assertEquals("Client not found.", thrown.getMessage());
        verify(clientRepository).findById(1L);
    }
}
