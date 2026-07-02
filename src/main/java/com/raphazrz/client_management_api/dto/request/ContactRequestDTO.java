package com.raphazrz.client_management_api.dto.request;

import com.raphazrz.client_management_api.enumerator.ContactType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ContactRequestDTO(
        @NotNull(message = "Contact type is required.")
        ContactType contactType,

        @NotBlank(message = "Contact is required.")
        String contact,

        @NotNull(message = "Client id is required.")
        Long clientId) {
}
