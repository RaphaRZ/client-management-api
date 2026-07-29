package com.raphazrz.client_management_api.service;

import com.raphazrz.client_management_api.dto.request.ClientRequestDTO;
import com.raphazrz.client_management_api.dto.response.ClientContactResponseDTO;
import com.raphazrz.client_management_api.dto.response.ClientResponseDTO;
import com.raphazrz.client_management_api.enumerator.ContactType;
import com.raphazrz.client_management_api.exception.ClientNotFoundException;
import com.raphazrz.client_management_api.exception.DuplicateDocumentException;
import com.raphazrz.client_management_api.mapper.ClientMapper;
import com.raphazrz.client_management_api.model.Client;
import com.raphazrz.client_management_api.repository.ClientRepository;
import net.datafaker.Faker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;


@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {
    private static final Faker faker = new Faker();

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
        ClientRequestDTO request = createClientRequestDTO();
        Client savedClient = new Client(request.firstName(), request.lastName(), request.document(), new ArrayList<>());
        ClientResponseDTO expectedResponse  = new ClientResponseDTO(request.firstName(), request.lastName(), request.document(), new ArrayList<>());
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
        List<ClientResponseDTO> expectedResponse  = clients.stream()
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


    private ClientRequestDTO createClientRequestDTO() {
        return new ClientRequestDTO(
                faker.name().firstName(),
                faker.name().lastName(),
                faker.number().digits(11)
        );
    }

    private Client createClient() {
        return new Client(
                faker.name().firstName(),
                faker.name().lastName(),
                faker.number().digits(11),
                new ArrayList<>()
        );
    }

    private ClientContactResponseDTO createClientContactResponseDTO() {
        return new ClientContactResponseDTO(
                faker.options().option(ContactType.values()),
                faker.text().text()
        );
    }
}
