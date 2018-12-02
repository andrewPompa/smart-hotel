package com.dynamics.andrzej.smart.hotel.controllers;

import com.dynamics.andrzej.smart.hotel.entities.Client;
import com.dynamics.andrzej.smart.hotel.models.ClientWithReservations;
import com.dynamics.andrzej.smart.hotel.services.ClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/api/client")
@Slf4j
public class ClientsController {
    private final ClientService clientService;

    public ClientsController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/all")
    public List<Client> getClients() {
        return clientService.findAll();
    }

    @GetMapping("{id}")
    public ClientWithReservations getClient(@PathVariable Long id) {
        log.info("client: {}", id);
        return clientService.findById(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        log.info("info: {}", id);
        clientService.delete(id);
    }
}
