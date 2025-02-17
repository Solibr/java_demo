package ru.t1.java.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.t1.java.demo.model.Client;

import java.util.Optional;
import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, Long> {
    @Override
    Optional<Client> findById(Long id);

    Optional<Client> findByClientId(UUID uuid);

    UUID deleteByClientId(UUID uuid);
}