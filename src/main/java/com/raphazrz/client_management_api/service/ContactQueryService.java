package com.raphazrz.client_management_api.service;

import com.raphazrz.client_management_api.exception.ContactNotFoundException;
import com.raphazrz.client_management_api.model.Contact;
import com.raphazrz.client_management_api.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class ContactQueryService {
    private final ContactRepository contactRepository;

    public Contact findContactById(Long contactId) {
        return contactRepository.findById(contactId)
                .orElseThrow(ContactNotFoundException::new);
    }
}
