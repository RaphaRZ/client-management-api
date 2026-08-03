package com.raphazrz.client_management_api.controller.docs;

import com.raphazrz.client_management_api.dto.request.ContactRequestDTO;
import com.raphazrz.client_management_api.dto.request.UpdateContactRequestDTO;
import com.raphazrz.client_management_api.dto.response.ContactResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;


public interface ContactControllerDocumentation {
    @Operation(summary = "Create a new contact.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Contact created successfully."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Client not found."
            )
    })
    ResponseEntity<ContactResponseDTO> createContact(ContactRequestDTO request);

    @Operation(summary = "Update contact by ID.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Contact updated successfully."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Contact not found."
            )
    })
    ResponseEntity<ContactResponseDTO> updateContact(Long id, UpdateContactRequestDTO request);
}
