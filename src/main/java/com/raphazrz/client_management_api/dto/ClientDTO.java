package com.raphazrz.client_management_api.dto;

import com.raphazrz.client_management_api.model.Contact;

import java.util.List;

public record ClientDTO(String firstName, String lastName, String document, List<Contact> contacts) {
}
