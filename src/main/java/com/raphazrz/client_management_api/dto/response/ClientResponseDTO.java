package com.raphazrz.client_management_api.dto.response;

import com.raphazrz.client_management_api.model.Contact;

import java.util.List;

public record ClientResponseDTO(String firstName, String lastName, String document, List<Contact> contacts) {
}
