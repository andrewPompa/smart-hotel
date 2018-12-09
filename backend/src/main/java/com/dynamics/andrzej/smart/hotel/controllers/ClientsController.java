package com.dynamics.andrzej.smart.hotel.controllers;

import com.dynamics.andrzej.smart.hotel.entities.Client;
import com.dynamics.andrzej.smart.hotel.models.ClientWithReservations;
import com.dynamics.andrzej.smart.hotel.services.ClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class ClientsController {
    private final ClientService clientService;

    public ClientsController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/client/pay")
    public boolean pay() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final UserDetails client = (UserDetails) authentication.getPrincipal();
        clientService.pay(client.getUsername());
        return true;
    }

    @GetMapping("/admin/api/client/all")
    public List<Client> getClients() {
        return clientService.findAll();
    }

    @GetMapping("/admin/api/client/{id}")
    public ClientWithReservations getClient(@PathVariable Long id) {
        log.info("client: {}", id);
        return clientService.findById(id);
    }

    @DeleteMapping("/admin/api/client/{id}")
    public void delete(@PathVariable Long id) {
        log.info("info: {}", id);
        clientService.delete(id);
    }

    @PostMapping("/register/client-form")
    public Client register(@RequestBody Client client) {
        return clientService.register(client);
    }

    @PostMapping("/client/api/update")
    public Client update(@RequestBody Client client) {
        return clientService.update(client);
    }

    @GetMapping("/client/api/info")
    public ClientWithReservations getClientInfo() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final UserDetails client = (UserDetails) authentication.getPrincipal();
        return clientService.findByLogin(client.getUsername());
    }
}
