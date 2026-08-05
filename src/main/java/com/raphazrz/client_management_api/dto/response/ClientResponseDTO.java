package com.raphazrz.client_management_api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;


public record ClientResponseDTO(
        @Schema(example = "1")
        Long id,

        @Schema(example = "Albert")
        String firstName,

        @Schema(example = "Wesker")
        String lastName,

        @Schema(example = "00000000001")
        String document,

        List<ClientContactResponseDTO> contacts) {
}
