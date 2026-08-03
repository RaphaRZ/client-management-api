package com.raphazrz.client_management_api.controller;

import com.raphazrz.client_management_api.controller.docs.ContactControllerDocumentation;
import com.raphazrz.client_management_api.dto.request.ContactRequestDTO;
import com.raphazrz.client_management_api.dto.request.UpdateContactRequestDTO;
import com.raphazrz.client_management_api.dto.response.ContactResponseDTO;
import com.raphazrz.client_management_api.service.ContactService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/contacts")
public class ContactController implements ContactControllerDocumentation {
    private final ContactService contactService;

    @PostMapping
    public ResponseEntity<ContactResponseDTO> createContact(@Valid @RequestBody ContactRequestDTO request) {
        ContactResponseDTO newContact = contactService.createContact(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(newContact);
    }

    @PutMapping("{id}")
    public ResponseEntity<ContactResponseDTO> updateContact(
            @PathVariable Long id,
            @Valid @RequestBody UpdateContactRequestDTO request) {
        ContactResponseDTO contact = contactService.updateContactById(id, request);

        return ResponseEntity.status(HttpStatus.OK).body(contact);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable Long id) {
        contactService.deleteContact(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
