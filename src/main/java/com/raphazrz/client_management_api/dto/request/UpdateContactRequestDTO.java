package com.raphazrz.client_management_api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record UpdateContactRequestDTO(
        @Schema(example = "1")
        @NotNull(message = "Contact type is required.")
        Integer contactType,

        @Schema(example = "41000000002")
        @NotBlank(message = "Contact is required.")
        String contact) {
}
