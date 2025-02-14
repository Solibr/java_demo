package ru.t1.java.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.t1.java.demo.dto.ClientDto;
import ru.t1.java.demo.service.ClientService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;

    @GetMapping
    public List<ClientDto> getAccounts() {
        return clientService.getClients();
    }

    @GetMapping("/{id}")
    public ClientDto getAccountById(@PathVariable UUID id) {
        return clientService.getClientById(id);
    }

    @PostMapping
    public ClientDto createAccount(ClientDto clientDto) {
        return clientService.createClient(clientDto);
    }

    @PutMapping("/{id}")
    public ClientDto updateAccount(@PathVariable UUID id, ClientDto clientDto) {
        return clientService.updateClientById(id, clientDto);
    }

    @DeleteMapping("/{id}")
    public UUID deleteAccount(@PathVariable UUID id) {
        return clientService.deleteById(id);
    }



}
