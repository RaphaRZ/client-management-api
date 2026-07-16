package com.raphazrz.client_management_api.service;

import com.raphazrz.client_management_api.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ClientQueryService {
    private final ClientRepository clientRepository;


}
