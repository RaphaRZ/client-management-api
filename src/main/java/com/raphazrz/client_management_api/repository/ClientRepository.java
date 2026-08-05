package com.raphazrz.client_management_api.repository;

import com.raphazrz.client_management_api.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByDocument(String document);

    boolean existsByDocument(String document);
    boolean existsByDocumentAndIdNot(String document, Long id);
}
