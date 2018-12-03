package com.dynamics.andrzej.smart.hotel.models;

import com.dynamics.andrzej.smart.hotel.entities.Client;
import com.dynamics.andrzej.smart.hotel.entities.Reservation;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class ClientWithReservations extends Client {
    private final List<Reservation> reservations;

    public ClientWithReservations(Client client, List<Reservation> reservations) {
        this.setId(client.getId());
        this.setFirstName(client.getFirstName());
        this.setLastName(client.getLastName());
        this.setDebt(client.getDebt());
        this.setLogin(client.getLogin());
        this.setCity(client.getCity());
        this.setStreet(client.getStreet());
        this.setFlatNumber(client.getFlatNumber());
        this.reservations = reservations;
    }

    public List<Reservation> getReservations() {
        return new ArrayList<>(reservations);
    }
}
