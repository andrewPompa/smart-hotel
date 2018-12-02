package com.dynamics.andrzej.smart.hotel.services;

import com.dynamics.andrzej.smart.hotel.entities.Client;
import com.dynamics.andrzej.smart.hotel.entities.Reservation;
import com.dynamics.andrzej.smart.hotel.models.ClientWithReservations;
import com.dynamics.andrzej.smart.hotel.respositories.ClientRepository;
import com.dynamics.andrzej.smart.hotel.respositories.ReservationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ClientService {
    private final ClientRepository clientRepository;
    private final ReservationRepository reservationRepository;
    private final PasswordEncoder passwordEncoder;

    public ClientService(ClientRepository clientRepository, ReservationRepository reservationRepository, PasswordEncoder passwordEncoder) {
        this.clientRepository = clientRepository;
        this.reservationRepository = reservationRepository;
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

    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    public void addDebtToClient(Client client, double roomPrice) {
        client.setDebt(client.getDebt() + roomPrice);
        clientRepository.save(client);
    }

    public void delete(Long id) {
        final Client client = clientRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Cannot load user!"));
        if (client.getDebt() > 0.0) {
            throw new IllegalArgumentException("User has debt!");
        }
        clientRepository.delete(client);
    }

    public ClientWithReservations findById(Long id) {
        final Client client = clientRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("user not found"));
        final List<Reservation> allByClientId = reservationRepository.findAllByClientId(client.getId());
        return new ClientWithReservations(client, allByClientId);
    }
}
