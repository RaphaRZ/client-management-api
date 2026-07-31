package com.raphazrz.client_management_api.controller;

import com.raphazrz.client_management_api.dto.request.ContactRequestDTO;
import com.raphazrz.client_management_api.dto.response.ContactResponseDTO;
import com.raphazrz.client_management_api.exception.ClientNotFoundException;
import com.raphazrz.client_management_api.service.ContactService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static com.raphazrz.client_management_api.util.TestDataFactory.createContactRequestDTO;
import static com.raphazrz.client_management_api.util.TestDataFactory.createContactResponseDTO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ContactController.class)
public class ContactControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    ContactService contactService;


    @Test
    @DisplayName("Should return status 201 Created.")
    void createContactCreated() throws Exception {
        // Arrange
        ContactRequestDTO request = createContactRequestDTO();
        ContactResponseDTO expectedResponse = createContactResponseDTO();

        when(contactService.createContact(request)).thenReturn(expectedResponse);

        // Act & Assert
        mockMvc.perform(post("/contacts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.contactType").value(expectedResponse.contactType().name()))
                .andExpect(jsonPath("$.contact").value(expectedResponse.contact()))
                .andExpect(jsonPath("$.clientId").value(expectedResponse.clientId()));

        verify(contactService).createContact(request);
    }

    @Test
    @DisplayName("Should return status 400 Bad Request.")
    void createContactBadRequest() throws Exception {
        // Arrange
        ContactRequestDTO request = new ContactRequestDTO(
                null,
                "",
                null
        );

        // Act & Assert
        mockMvc.perform(
                        post("/contacts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest());

        verify(contactService, never()).createContact(any(ContactRequestDTO.class));
    }

    @Test
    @DisplayName("Should return status 404 Not Found.")
    void createContactClientNotFoundException() throws Exception {
        // Arrange
        ContactRequestDTO request = createContactRequestDTO();

        when(contactService.createContact(request))
                .thenThrow(new ClientNotFoundException());

        // Act & Assert
        mockMvc.perform(
                        post("/contacts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isNotFound());

        verify(contactService).createContact(request);
    }
}
