package ru.t1.java.demo.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.t1.java.demo.dto.ClientDto;
import ru.t1.java.demo.mapper.ClientMapper;
import ru.t1.java.demo.model.Client;
import ru.t1.java.demo.repository.ClientRepository;
import ru.t1.java.demo.service.ClientService;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository repository;

    private final ClientMapper clientMapper;

    @Override
    public List<ClientDto> getClients() {
        return repository.findAll().stream()
                .map(clientMapper::toDto)
                .toList();
    }

    @Override
    public ClientDto getClientById(UUID uuid) {
        Client client = repository.findByClientId(uuid).orElseThrow();
        return clientMapper.toDto(client);
    }

    @Override
    public ClientDto createClient(ClientDto clientDto) {
        Client client = clientMapper.toEntity(clientDto);
        client.setClientId(UUID.randomUUID());
        Client savedClient = repository.save(client);
        return clientMapper.toDto(savedClient);
    }

    @Override
    public ClientDto updateClientById(UUID uuid, ClientDto clientDto) {
        Client clientToUpdate = repository.findByClientId(uuid).orElseThrow();
        Client client = clientMapper.toEntity(clientDto);
        clientToUpdate.setFirstName(client.getFirstName());
        clientToUpdate.setLastName(client.getLastName());
        clientToUpdate.setMiddleName(client.getMiddleName());
        Client updatedClient = repository.save(clientToUpdate);
        return clientMapper.toDto(updatedClient);
    }

    @Override
    public UUID deleteById(UUID uuid) {
        return repository.deleteByClientId(uuid);
    }
}
