package com.raphazrz.client_management_api.mapper;

import com.raphazrz.client_management_api.dto.request.ContactRequestDTO;
import com.raphazrz.client_management_api.dto.response.ContactResponseDTO;
import com.raphazrz.client_management_api.enumerator.ContactType;
import com.raphazrz.client_management_api.model.Contact;

public class ContactMapper {
    public static ContactResponseDTO toResponseDTO(Contact contact) {
        return new ContactResponseDTO(
                contact.getContactType(),
                contact.getContact(),
                contact.getClientId()
        );
    }

    public static Contact toEntity(ContactRequestDTO requestDTO) {
        Contact contact = new Contact();

        contact.setContactType(ContactType.fromType(requestDTO.contactType()));
        contact.setContact(requestDTO.contact());

        return contact;
    }
}
