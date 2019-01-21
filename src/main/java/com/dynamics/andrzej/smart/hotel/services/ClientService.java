package com.dynamics.andrzej.smart.hotel.services;

import com.dynamics.andrzej.smart.hotel.entities.Client;
import com.dynamics.andrzej.smart.hotel.entities.Reservation;
import com.dynamics.andrzej.smart.hotel.models.ClientWithReservations;
import com.dynamics.andrzej.smart.hotel.respositories.ClientRepository;
import com.dynamics.andrzej.smart.hotel.respositories.ReservationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
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

    public Client register(Client client) {
        if (client.getLogin() == null || client.getLogin().isEmpty()) {
            throw new IllegalArgumentException("login has to be set");
        }
        if (client.getPassword() == null || client.getPassword().isEmpty()) {
            throw new IllegalArgumentException("password has to be set");
        }
        if (client.getFirstName() == null || client.getFirstName().isEmpty()) {
            throw new IllegalArgumentException("firstname has to be set");
        }
        if (client.getLastName() == null || client.getLastName().isEmpty()) {
            throw new IllegalArgumentException("lastname has to be set");
        }
        if (client.getCity() == null || client.getCity().isEmpty()) {
            throw new IllegalArgumentException("city has to be set");
        }
        if (client.getStreet() == null || client.getStreet().isEmpty()) {
            throw new IllegalArgumentException("street has to be set");
        }
        if (client.getFlatNumber() == null || client.getFlatNumber().isEmpty()) {
            throw new IllegalArgumentException("flat number has to be set");
        }
        client.setPassword(passwordEncoder.encode(client.getPassword()));
        client.setDebt(0.0);
        return clientRepository.save(client);
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

    public void subtractDebt(Client client, double roomPrice) {
        client.setDebt(client.getDebt() - roomPrice);
        clientRepository.save(client);
    }

    public void pay(String email) {
        final Client client = clientRepository.findByLoginEquals(email).orElseThrow(() -> new IllegalArgumentException("user not found"));
        client.setDebt(0.0);
        clientRepository.save(client);
    }

    public void delete(Long id) {
        final Client client = clientRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Cannot load user!"));
        if (client.getDebt() > 0.0) {
            throw new IllegalArgumentException("User has debt!");
        }
        clientRepository.delete(client);
    }

    public ClientWithReservations findByLogin(String email) {
        final Client client = clientRepository.findByLoginEquals(email).orElseThrow(() -> new IllegalArgumentException("user not found"));
        return getClientWithReservations(client);
    }


    public ClientWithReservations findById(Long id) {
        final Client client = clientRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("user not found"));
        return getClientWithReservations(client);
    }

    public Client update(Client client) {
        final Client fromDb = clientRepository.findById(client.getId()).orElseThrow(() -> new IllegalArgumentException("not found"));
        fromDb.setFirstName(client.getFirstName());
        fromDb.setLastName(client.getLastName());
        fromDb.setCity(client.getCity());
        fromDb.setStreet(client.getStreet());
        fromDb.setFlatNumber(client.getFlatNumber());
        return clientRepository.save(fromDb);
    }

    private ClientWithReservations getClientWithReservations(Client client) {
        final List<Reservation> allByClientId = reservationRepository.findAllByClientId(client.getId());
        allByClientId.forEach(reservation -> reservation.getRooms().forEach(room -> room.setReservations(Collections.emptyList())));
        return new ClientWithReservations(client, allByClientId);
    }
}
