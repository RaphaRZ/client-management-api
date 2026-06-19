package com.raphazrz.client_management_api.dto;

import com.raphazrz.client_management_api.enumerator.ContactType;

public record ContactDTO(ContactType contactType, String contact) {
}
