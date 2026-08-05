package com.raphazrz.client_management_api.controller;

import com.raphazrz.client_management_api.controller.docs.ClientControllerDocumentation;
import com.raphazrz.client_management_api.dto.request.UpdateClientRequestDTO;
import com.raphazrz.client_management_api.dto.response.ClientContactResponseDTO;
import com.raphazrz.client_management_api.dto.request.ClientRequestDTO;
import com.raphazrz.client_management_api.dto.response.ClientResponseDTO;
import com.raphazrz.client_management_api.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/clients")
public class ClientController implements ClientControllerDocumentation {
    private final ClientService clientService;


    @PostMapping
    public ResponseEntity<ClientResponseDTO> createClient(@Valid @RequestBody ClientRequestDTO request) {
        ClientResponseDTO newClient = clientService.createClient(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(newClient);
    }

    @GetMapping
    public ResponseEntity<List<ClientResponseDTO>> getClients() {
        List<ClientResponseDTO> clients = clientService.getClients();

        return ResponseEntity.status(HttpStatus.OK).body(clients);
    }

    @GetMapping("{id}")
    public ResponseEntity<ClientResponseDTO> getClientById(@PathVariable Long id) {
        ClientResponseDTO client = clientService.getClientById(id);

        return ResponseEntity.status(HttpStatus.OK).body(client);
    }

    @GetMapping("{id}/contacts")
    public ResponseEntity<List<ClientContactResponseDTO>> getAllContactsByClientId(@PathVariable Long id) {
        List<ClientContactResponseDTO> contacts = clientService.getAllContactsByClientId(id);

        return ResponseEntity.status(HttpStatus.OK).body(contacts);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> updateClientById(
            @PathVariable Long id,
            @Valid @RequestBody UpdateClientRequestDTO request) {
        ClientResponseDTO updatedClient = clientService.updateClientById(id, request);

        return ResponseEntity.status(HttpStatus.OK).body(updatedClient);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClientById(@PathVariable Long id) {
        clientService.deleteClientById(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
