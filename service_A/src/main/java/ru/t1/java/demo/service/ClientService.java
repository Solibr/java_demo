package ru.t1.java.demo.service;

import ru.t1.java.demo.dto.ClientDto;

import java.util.List;
import java.util.UUID;

public interface ClientService {
    List<ClientDto> getClients();

    ClientDto getClientById(UUID id);

    ClientDto createClient(ClientDto client);

    ClientDto updateClientById(UUID id, ClientDto client);

    UUID deleteById(UUID id);
}
