package com.raphazrz.client_management_api.controller.docs;

import com.raphazrz.client_management_api.dto.other.ExceptionDTO;
import com.raphazrz.client_management_api.dto.request.ContactRequestDTO;
import com.raphazrz.client_management_api.dto.request.UpdateContactRequestDTO;
import com.raphazrz.client_management_api.dto.response.ContactResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;

import static com.raphazrz.client_management_api.constant.SwaggerMessageErrors.*;


public interface ContactControllerDocumentation {
    @Operation(summary = "Create a new contact.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Contact created successfully."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = INVALID_CONTACT_TYPE_DESCRIPTION,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class),
                            examples = @ExampleObject(value = INVALID_CONTACT_TYPE_VALUE)
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
    ResponseEntity<ContactResponseDTO> createContact(ContactRequestDTO request);

    @Operation(summary = "Update contact by ID.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Contact updated successfully."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = INVALID_CONTACT_TYPE_DESCRIPTION,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class),
                            examples = @ExampleObject(value = INVALID_CONTACT_TYPE_VALUE)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = CONTACT_NOT_FOUND_DESCRIPTION,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class),
                            examples = @ExampleObject(value = CONTACT_NOT_FOUND_VALUE)
                    )
            )
    })
    ResponseEntity<ContactResponseDTO> updateContact(Long id, UpdateContactRequestDTO request);

    @Operation(summary = "Delete contact by ID.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Contact deleted successfully."
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
                    description = CONTACT_NOT_FOUND_DESCRIPTION,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class),
                            examples = @ExampleObject(value = CONTACT_NOT_FOUND_VALUE)
                    )
            )
    })
    ResponseEntity<Void> deleteContact(Long id);
}
