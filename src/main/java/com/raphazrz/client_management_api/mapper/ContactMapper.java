package com.raphazrz.client_management_api.mapper;

import com.raphazrz.client_management_api.dto.request.ContactRequestDTO;
import com.raphazrz.client_management_api.dto.response.ContactResponseDTO;
import com.raphazrz.client_management_api.model.Client;
import com.raphazrz.client_management_api.model.Contact;

public class ContactMapper {
    public static ContactResponseDTO toResponseDTO(Contact contact) {
        return new ContactResponseDTO(
                contact.getContactType(),
                contact.getContact(),
                contact.getClientId()
        );
    }

    public static Contact toEntity(ContactRequestDTO contactRequestDTO) {
        return new Contact(
                contactRequestDTO.contactType(),
                contactRequestDTO.contact(),
                new Client()
        );
    }
}
