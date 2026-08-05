package com.raphazrz.client_management_api.dto.response;

import com.raphazrz.client_management_api.enumerator.ContactType;
import io.swagger.v3.oas.annotations.media.Schema;


public record ContactResponseDTO(
        @Schema(example = "1")
        Long id,

        @Schema(example = "PHONE")
        ContactType contactType,

        @Schema(example = "41000000001")
        String contact,

        @Schema(example = "1")
        Long clientId) {
}