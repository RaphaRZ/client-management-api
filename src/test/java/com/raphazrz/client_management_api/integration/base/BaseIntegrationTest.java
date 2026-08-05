package com.raphazrz.client_management_api.integration.base;

import com.raphazrz.client_management_api.dto.request.ClientRequestDTO;
import com.raphazrz.client_management_api.dto.request.ContactRequestDTO;
import com.raphazrz.client_management_api.dto.request.UpdateClientRequestDTO;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import tools.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public abstract class BaseIntegrationTest {
    private static final String BASE_CONTACTS_URL = "/contacts";
    private static final String BASE_CLIENTS_URL = "/clients";

    @Container
    protected static final PostgreSQLContainer postgres =
            new PostgreSQLContainer("postgres:18")
                    .withDatabaseName("client_manager")
                    .withUsername("postgres")
                    .withPassword("postgres");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;


    protected ClientRequestDTO createClientViaApi(ClientRequestDTO request) throws Exception {
        performPostClient(request)
                .andExpect(status().isCreated());

        return request;
    }

    protected ContactRequestDTO createContactViaApi(ContactRequestDTO request) throws Exception {
        performPostContact(request)
                .andExpect(status().isCreated());

        return request;
    }

    protected ResultActions performPostClient(ClientRequestDTO request) throws Exception {
        return mockMvc.perform(
                post(BASE_CLIENTS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );
    }

    protected ResultActions performPostContact(ContactRequestDTO request) throws Exception {
        return mockMvc.perform(
                post(BASE_CONTACTS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));
    }

    protected ResultActions performGetClients() throws Exception {
        return mockMvc.perform(get(BASE_CLIENTS_URL));
    }

    protected ResultActions performGetClientById(Long id) throws Exception {
        return mockMvc.perform(get(BASE_CLIENTS_URL + "/{id}", id));
    }

    protected ResultActions performGetAllContactsByClientId(Long id) throws Exception {
        return mockMvc.perform(get(BASE_CLIENTS_URL + "/{id}/contacts", id));
    }

    protected ResultActions performPutClientById(Long id, UpdateClientRequestDTO request) throws Exception {
        return mockMvc.perform(
                put(BASE_CLIENTS_URL + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );
    }

    protected ResultActions performDeleteClientById(Long id) throws Exception {
        return mockMvc.perform(delete(BASE_CLIENTS_URL + "/{id}", id));
    }
}