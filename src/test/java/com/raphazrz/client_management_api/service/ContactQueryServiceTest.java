package com.raphazrz.client_management_api.service;

import com.raphazrz.client_management_api.repository.ClientRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ContactQueryServiceTest {
    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ContactQueryService contactQueryService;



}
