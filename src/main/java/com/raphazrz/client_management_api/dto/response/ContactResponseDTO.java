package com.raphazrz.client_management_api.dto.response;

import com.raphazrz.client_management_api.enumerator.ContactType;

public record ContactResponseDTO(ContactType contactType, String contact, Long clientId) {
}
