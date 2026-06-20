package com.raphazrz.client_management_api.dto;

import com.raphazrz.client_management_api.enumerator.ContactType;
import com.raphazrz.client_management_api.model.Client;

public record ContactDTO(ContactType contactType, String contact, Client client) {
}
