package com.raphazrz.client_management_api.service;

import com.raphazrz.client_management_api.model.Contact;
import com.raphazrz.client_management_api.repository.ContactRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.raphazrz.client_management_api.util.TestDataFactory.createContact;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class ContactQueryServiceTest {
    @Mock
    private ContactRepository contactRepository;

    @InjectMocks
    private ContactQueryService contactQueryService;


    @Test
    @DisplayName("Should return the contact successfully.")
    void findContactByIdSuccess() {
        // Arrange
        Contact contact = createContact();
        when(contactRepository.findById(1L)).thenReturn(Optional.of(contact));

        // Act
        Contact result = contactQueryService.findContactById(1L);

        // Assert
        assertEquals(contact, result);
        verify(contactRepository).findById(1L);
    }
}
