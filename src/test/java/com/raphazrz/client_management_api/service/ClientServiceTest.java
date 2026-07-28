package com.raphazrz.client_management_api.service;

import com.raphazrz.client_management_api.dto.request.ClientRequestDTO;
import com.raphazrz.client_management_api.dto.response.ClientResponseDTO;
import com.raphazrz.client_management_api.model.Client;
import com.raphazrz.client_management_api.repository.ClientRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


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
    @DisplayName("Should return a new client as ClientResponseDTO.")
    void createClientSuccess() {
        // Arrange
        ClientRequestDTO request = new ClientRequestDTO("First", "Client", "00123456789");
        Client savedClient = new Client("First", "Client", "00123456789", new ArrayList<>());
        ClientResponseDTO expectedResponse  = new ClientResponseDTO("First", "Client", "00123456789", new ArrayList<>());
        when(clientRepository.existsByDocument(any(String.class))).thenReturn(false);
        when(clientRepository.save(any(Client.class))).thenReturn(savedClient);

        // Act
        ClientResponseDTO result = clientService.createClient(request);

        // Assert
        assertEquals(expectedResponse , result);
        verify(clientRepository).existsByDocument(any(String.class));
        verify(clientRepository).save(any(Client.class));
    }
}
