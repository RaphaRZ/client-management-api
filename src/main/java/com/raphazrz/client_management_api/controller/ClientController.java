package com.raphazrz.client_management_api.controller;

import com.raphazrz.client_management_api.dto.ClientDTO;
import com.raphazrz.client_management_api.model.Client;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/clients")
public class ClientController {
    @PostMapping
    public ResponseEntity<Client> createClient(@Valid @RequestBody ClientDTO request) {
        Client newClient = this.clientService.createClient(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(newClient);
    }
}
