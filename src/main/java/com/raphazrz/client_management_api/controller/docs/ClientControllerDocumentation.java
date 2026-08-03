package com.raphazrz.client_management_api.controller.docs;

import com.raphazrz.client_management_api.constants.SwaggerMessageErrors;
import com.raphazrz.client_management_api.dto.other.ExceptionDTO;
import com.raphazrz.client_management_api.dto.request.ClientRequestDTO;
import com.raphazrz.client_management_api.dto.request.UpdateClientRequestDTO;
import com.raphazrz.client_management_api.dto.response.ClientContactResponseDTO;
import com.raphazrz.client_management_api.dto.response.ClientResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.raphazrz.client_management_api.constants.SwaggerMessageErrors.*;


public interface ClientControllerDocumentation {
    @Operation(summary = "Create a new client.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Client created successfully."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = VALIDATION_FAILED_DESCRIPTION,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class),
                            examples = @ExampleObject(value = VALIDATION_FAILED_VALUE)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = DOCUMENT_ALREADY_REGISTERED_DESCRIPTION,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class),
                            examples = @ExampleObject(value = DOCUMENT_ALREADY_REGISTERED_VALUE)
                    )
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
                    description = INVALID_PARAMETER_ID_DESCRIPTION,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class),
                            examples = @ExampleObject(value = INVALID_PARAMETER_ID_VALUE)
                    )

            ),
            @ApiResponse(
                    responseCode = "404",
                    description = CLIENT_NOT_FOUND_DESCRIPTION,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class),
                            examples = @ExampleObject(value = CLIENT_NOT_FOUND_VALUE)
                    )
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
                    description = INVALID_PARAMETER_ID_DESCRIPTION,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class),
                            examples = @ExampleObject(value = INVALID_PARAMETER_ID_VALUE)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = CLIENT_NOT_FOUND_DESCRIPTION,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class),
                            examples = @ExampleObject(value = CLIENT_NOT_FOUND_VALUE)
                    )
            )
    })
    ResponseEntity<List<ClientContactResponseDTO>> getAllContactsByClientId(Long clientId);

    @Operation(summary = "Update client by ID.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Client updated successfully."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = VALIDATION_FAILED_DESCRIPTION,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class),
                            examples = @ExampleObject(value = VALIDATION_FAILED_VALUE)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = SwaggerMessageErrors.CLIENT_NOT_FOUND_DESCRIPTION,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class),
                            examples = @ExampleObject(value = CLIENT_NOT_FOUND_VALUE)
                    )

            ),
            @ApiResponse(
                    responseCode = "409",
                    description = DOCUMENT_ALREADY_REGISTERED_DESCRIPTION,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class),
                            examples = @ExampleObject(value = DOCUMENT_ALREADY_REGISTERED_VALUE)
    )
            )
    })
    ResponseEntity<ClientResponseDTO> updateClientById(Long id, UpdateClientRequestDTO request);

    @Operation(summary = "Delete client by ID.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Client deleted successfully."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = INVALID_PARAMETER_ID_DESCRIPTION,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class),
                            examples = @ExampleObject(value = INVALID_PARAMETER_ID_VALUE)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = SwaggerMessageErrors.CLIENT_NOT_FOUND_DESCRIPTION,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class),
                            examples = @ExampleObject(value = CLIENT_NOT_FOUND_VALUE)
                    )
            )
    })
    ResponseEntity<Void> deleteClientById(Long id);
}
