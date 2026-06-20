package com.raphazrz.client_management_api.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "client")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String firstName;

    private String lastName;

    private String document;

    private List<Contact> contacts;

    @Builder
    public Client(String firstName, String lastName, String document, List<Contact> contacts) {}
}
