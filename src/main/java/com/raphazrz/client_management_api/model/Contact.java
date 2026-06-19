package com.raphazrz.client_management_api.model;

import com.raphazrz.client_management_api.enumerator.ContactType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "contact")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private ContactType contactType;

    private String contact;

    private Client client;
}
