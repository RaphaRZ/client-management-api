package com.raphazrz.client_management_api.dto.request;

import com.raphazrz.client_management_api.model.Contact;

import java.util.List;

public record ClientRequestDTO(String firstName, String lastName, String document, List<Contact> contacts) {
}
