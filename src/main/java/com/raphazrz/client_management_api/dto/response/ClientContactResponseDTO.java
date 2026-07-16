package com.raphazrz.client_management_api.dto.response;

import com.raphazrz.client_management_api.enumerator.ContactType;


public record ClientContactResponseDTO(
        ContactType contactType,
        String contact) {
}
