package com.raphazrz.client_management_api.controller;

import com.raphazrz.client_management_api.dto.request.ContactRequestDTO;
import com.raphazrz.client_management_api.dto.request.UpdateContactRequestDTO;
import com.raphazrz.client_management_api.dto.response.ContactResponseDTO;
import com.raphazrz.client_management_api.exception.ClientNotFoundException;
import com.raphazrz.client_management_api.exception.ContactNotFoundException;
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
import static com.raphazrz.client_management_api.util.TestDataFactory.createUpdateContactRequestDTO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ContactController.class)
public class ContactControllerTest {
    private static final String BASE_URL = "/contacts";

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
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(expectedResponse.id()))
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
                        post(BASE_URL)
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
                        post(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isNotFound());

        verify(contactService).createContact(request);
    }

    @Test
    @DisplayName("Should return status 200 Ok.")
    void updateContactByIdOk() throws Exception {
        // Arrange
        Long id = 1L;
        UpdateContactRequestDTO request = createUpdateContactRequestDTO();
        ContactResponseDTO expectedResponse = createContactResponseDTO();

        when(contactService.updateContactById(id, request))
                .thenReturn(expectedResponse);

        // Act & Assert
        mockMvc.perform(
                        put(BASE_URL + "/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedResponse.id()))
                .andExpect(jsonPath("$.contactType").value(expectedResponse.contactType().name()))
                .andExpect(jsonPath("$.contact").value(expectedResponse.contact()))
                .andExpect(jsonPath("$.clientId").value(expectedResponse.clientId()));

        verify(contactService).updateContactById(id, request);
    }

    @Test
    @DisplayName("Should return status 400 Bad Request.")
    void updateContactByIdBadRequest() throws Exception {
        // Arrange
        Long id = 1L;
        UpdateContactRequestDTO request = new UpdateContactRequestDTO(
                null,
                ""
        );

        // Act & Assert
        mockMvc.perform(
                        put(BASE_URL + "/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest());

        verify(contactService, never())
                .updateContactById(any(Long.class), any(UpdateContactRequestDTO.class));
    }

    @Test
    @DisplayName("Should return status 404 Not Found.")
    void updateContactByIdContactNotFoundException() throws Exception {
        // Arrange
        Long id = 1L;
        UpdateContactRequestDTO request = createUpdateContactRequestDTO();

        when(contactService.updateContactById(id, request))
                .thenThrow(new ContactNotFoundException());

        // Act & Assert
        mockMvc.perform(
                        put(BASE_URL + "/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isNotFound());

        verify(contactService).updateContactById(id, request);
    }

    @Test
    @DisplayName("Should return status 204 No Content.")
    void deleteContactNoContent() throws Exception {
        // Arrange
        Long id = 1L;

        doNothing().when(contactService).deleteContact(id);

        // Act & Assert
        mockMvc.perform(delete(BASE_URL + "/{id}", id))
                .andExpect(status().isNoContent());

        verify(contactService).deleteContact(id);
    }

    @Test
    @DisplayName("Should return status 404 Not Found.")
    void deleteContactClientNotFoundException() throws Exception {
        // Arrange
        Long id = 1L;

        doThrow(new ClientNotFoundException())
                .when(contactService)
                .deleteContact(id);

        // Act & Assert
        mockMvc.perform(delete(BASE_URL + "/{id}", id))
                .andExpect(status().isNotFound());

        verify(contactService).deleteContact(id);
    }
}
