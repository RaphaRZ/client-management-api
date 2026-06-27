package com.raphazrz.client_management_api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Builder;

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

    @OneToMany(mappedBy = "client")
    private List<Contact> contacts;

    @Builder
    public Client(String firstName, String lastName, String document, List<Contact> contacts) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.document = document;
        this.contacts = contacts;
    }
}
