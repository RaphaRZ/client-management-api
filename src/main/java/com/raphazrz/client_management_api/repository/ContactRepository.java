package com.raphazrz.client_management_api.repository;

import com.raphazrz.client_management_api.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Long> {
}
