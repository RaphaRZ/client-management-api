package com.raphazrz.client_management_api.util;

import com.raphazrz.client_management_api.dto.request.ClientRequestDTO;
import com.raphazrz.client_management_api.dto.request.ContactRequestDTO;
import com.raphazrz.client_management_api.dto.request.UpdateClientRequestDTO;
import com.raphazrz.client_management_api.dto.response.ClientContactResponseDTO;
import com.raphazrz.client_management_api.enumerator.ContactType;
import com.raphazrz.client_management_api.model.Client;
import net.datafaker.Faker;

import java.util.ArrayList;


public class TestDataFactory {
    private static final Faker faker = new Faker();


    public static ClientRequestDTO createClientRequestDTO() {
        return new ClientRequestDTO(
                faker.name().firstName(),
                faker.name().lastName(),
                faker.number().digits(11)
        );
    }

    public static Client createClient() {
        return new Client(
                faker.name().firstName(),
                faker.name().lastName(),
                faker.number().digits(11),
                new ArrayList<>()
        );
    }

    public static ClientContactResponseDTO createClientContactResponseDTO() {
        return new ClientContactResponseDTO(
                faker.options().option(ContactType.values()),
                faker.text().text()
        );
    }

    public static UpdateClientRequestDTO createUpdateClientRequestDTO() {
        return new UpdateClientRequestDTO(
                faker.name().firstName(),
                faker.name().lastName(),
                faker.number().digits(11)
        );
    }

    public static ContactRequestDTO createContactRequestDTO() {
        return new ContactRequestDTO(
                faker.number().numberBetween(1, 3),
                faker.text().text(),
                faker.number().randomNumber()
        );
    }
}
