package com.raphazrz.client_management_api.dto.other;

import com.raphazrz.client_management_api.enumerator.ContactType;


public record ClientContactDTO(
        ContactType contactType,
        String contact) {
}
