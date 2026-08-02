package com.raphazrz.client_management_api.controller.docs;

import com.raphazrz.client_management_api.dto.request.ClientRequestDTO;
import com.raphazrz.client_management_api.dto.request.UpdateClientRequestDTO;
import com.raphazrz.client_management_api.dto.response.ClientResponseDTO;
import com.raphazrz.client_management_api.dto.response.ContactResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ClientControllerDocumentation {
    @Operation(summary = "Create a new client.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Client created successfully."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data."
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Document already registered."
            )
    })
    ResponseEntity<ClientResponseDTO> createClient(ClientRequestDTO request);

    @Operation(summary = "Get all clients.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Clients retrieved successfully."
            )
    })
    ResponseEntity<List<ClientResponseDTO>> getClients();

    @Operation(summary = "Get client by ID.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Client retrieved successfully."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid value for parameter 'id'."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Client not found."
            )
    })
    ResponseEntity<ClientResponseDTO> getClientById(Long id);

    @Operation(summary = "Get all contacts from a client.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Contacts retrieved successfully."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid value for parameter 'id'."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Client not found."
            )
    })
    ResponseEntity<List<ContactResponseDTO>> getAllContactsByClientId(Long clientId);

    @Operation(summary = "Update client by ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Client updated successfully."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Client not found."
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Document already registered."
            )
    })
    ResponseEntity<ClientResponseDTO> updateClientById(Long id, UpdateClientRequestDTO request);
}
