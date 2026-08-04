package com.raphazrz.client_management_api.integration.controller;

import com.raphazrz.client_management_api.integration.base.BaseIntegrationTest;
import com.raphazrz.client_management_api.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;


public class ClientControllerIntegrationTest extends BaseIntegrationTest {
    private static final String URL = "/clients";

    @Autowired
    private ClientRepository clientRepository;

    @BeforeEach
    void setUp() {
        clientRepository.deleteAll();
    }
}
