package com.raphazrz.client_management_api.service;

import com.raphazrz.client_management_api.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class ContactQueryService {
    private final ContactRepository contactRepository;


}
