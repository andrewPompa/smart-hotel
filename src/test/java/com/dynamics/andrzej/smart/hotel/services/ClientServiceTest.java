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
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.testng.Assert.*;

public class ClientServiceTest {
    private RoomService roomService;
    private ClientService clientService;
    private RoomRepository roomRepository;

    public void setUp() {
        ConfigurableApplicationContext run = SpringApplication.run(SmartHotel.class, "");
        roomService = run.getBean(RoomService.class);
        clientService = run.getBean(ClientService.class);
        roomRepository = run.getBean(RoomRepository.class);
    }

    @Test
    @Ignore
    public void registerDeleteClientTest(){
        final List<Client> beforeRegister = clientService.findAll();
        int initialSize = beforeRegister.size();
        final Client client = clientService.register("test@test.pl", "Jan", "Nowak", "haslo");
        final List<Client> afterRegister= clientService.findAll();
        Assert.assertEquals(afterRegister.size(),initialSize +1 );
        clientService.delete(afterRegister.get(0).getId());
        final List<Client> afterDelete= clientService.findAll();
        Assert.assertEquals(afterDelete.size(),initialSize);
    }

    @Test
    @Ignore
    public void testAddDebtToClient() {
        final Client client = clientService.register("test@test.pl", "Jan", "Nowak", "haslo");
        double debt = client.getDebt();
        double addedDebt = 100.0;
        clientService.addDebtToClient(client, addedDebt);
        Assert.assertEquals(addedDebt- debt, client.getDebt());
    }

    @Test
    @Ignore
    public void testSubtractDebt() {
        final Client client = clientService.register("test@test.pl", "Jan", "Nowak", "haslo");
        double debt = client.getDebt();
        double subDebt = 100.0;
        clientService.subtractDebt(client, subDebt);
        Assert.assertEquals(-subDebt, client.getDebt());
    }

    @Test
    @Ignore
    public void testPay() {
        final Client client = clientService.register("test@test.pl", "Jan", "Nowak", "haslo");

        clientService.addDebtToClient(client, 150.0);
        Assert.assertEquals(150.0, client.getDebt());
//        clientService.pay(client.getLogin());
        client.setDebt(0.0);
        Assert.assertEquals(0.0, client.getDebt());

    }


    @Test
    @Ignore
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