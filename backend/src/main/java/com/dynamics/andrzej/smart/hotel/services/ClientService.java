package com.dynamics.andrzej.smart.hotel.services;

import com.dynamics.andrzej.smart.hotel.entities.Client;
import com.dynamics.andrzej.smart.hotel.respositories.ClientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class ClientService {
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    public ClientService(ClientRepository clientRepository, PasswordEncoder passwordEncoder) {
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<Client> getClientByEmail(String email) {
        return clientRepository.findByLoginEquals(email);
    }

    public Client register(String email, String firstName, String lastName, String password) {
        Client client = new Client();
        client.setFirstName(firstName);
        client.setLastName(lastName);
        client.setLogin(email);
        client.setDebt(0.0);
        client.setPassword(passwordEncoder.encode(password));
        return clientRepository.save(client);
    }
}
