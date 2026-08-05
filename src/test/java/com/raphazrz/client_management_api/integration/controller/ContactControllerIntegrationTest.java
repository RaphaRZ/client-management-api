package com.raphazrz.client_management_api.integration.controller;

import com.raphazrz.client_management_api.integration.base.BaseIntegrationTest;
import com.raphazrz.client_management_api.repository.ClientRepository;
import com.raphazrz.client_management_api.repository.ContactRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;


public class ContactControllerIntegrationTest extends BaseIntegrationTest {
    private static final String BASE_CONTACTS_URL = "/contacts";
    private static final String BASE_CLIENTS_URL = "/clients";

    @Autowired
    ContactRepository contactRepository;

    @Autowired
    ClientRepository clientRepository;

    @BeforeEach
    void setUp() {
        clientRepository.deleteAll();
    }



}
