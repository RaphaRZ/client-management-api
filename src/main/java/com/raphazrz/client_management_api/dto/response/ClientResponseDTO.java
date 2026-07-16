package com.raphazrz.client_management_api.dto.response;

import java.util.List;

public record ClientResponseDTO(String firstName, String lastName, String document, List<ClientContactResponseDTO> contacts) {
}
