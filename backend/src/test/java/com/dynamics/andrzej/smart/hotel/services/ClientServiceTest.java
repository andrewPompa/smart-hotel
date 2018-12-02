package com.dynamics.andrzej.smart.hotel.services;

import com.dynamics.andrzej.smart.hotel.SmartHotel;
import com.dynamics.andrzej.smart.hotel.entities.Client;
import com.dynamics.andrzej.smart.hotel.entities.Room;
import com.dynamics.andrzej.smart.hotel.models.ClientWithReservations;
import com.dynamics.andrzej.smart.hotel.models.ReservationRequest;
import com.dynamics.andrzej.smart.hotel.models.ReservationResponse;
import com.dynamics.andrzej.smart.hotel.respositories.RoomRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.stream.Collectors;

import static org.testng.Assert.*;

public class ClientServiceTest {
    private RoomService roomService;
    private ClientService clientService;
    private RoomRepository roomRepository;

    @BeforeTest
    public void setUp() {
        ConfigurableApplicationContext run = SpringApplication.run(SmartHotel.class, "");
        roomService = run.getBean(RoomService.class);
        clientService = run.getBean(ClientService.class);
        roomRepository = run.getBean(RoomRepository.class);
    }

    @Test
    public void testFindById() {
        final Client client = clientService.register("test@test.pl", "Andrzej", "Pompa", "test");
        final ReservationRequest reservationRequest = new ReservationRequest();
        reservationRequest.setEmail(client.getLogin());
        reservationRequest.setFirstName(client.getFirstName());
        reservationRequest.setLastName(client.getLastName());
        reservationRequest.setFrom(new Date());
        reservationRequest.setTo(new Date());
        reservationRequest.setRoomIds(roomRepository.findAll().stream().map(Room::getId).collect(Collectors.toList()));
        final ReservationResponse reserve = roomService.reserve(reservationRequest);
        assert reserve != null;
        final ClientWithReservations clientToTest = clientService.findById(client.getId());
        Assert.assertFalse(clientToTest.getReservations().isEmpty());
    }
}