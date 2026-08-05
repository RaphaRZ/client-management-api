package com.raphazrz.client_management_api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;


public record ClientRequestDTO(
        @Schema(example = "Albert")
        @NotBlank(message = "First name is required.")
        String firstName,

        @Schema(example = "Wesker")
        @NotBlank(message = "Last name is required.")
        String lastName,

        @Schema(example = "000.000.000-01")
        @NotBlank(message = "Document is required.")
        @Pattern(
                regexp = "^\\d{11}$",
                message = "Document must contain exactly 11 digits."
        )
        String document) {
}
