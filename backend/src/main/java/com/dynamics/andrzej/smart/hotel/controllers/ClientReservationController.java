package com.dynamics.andrzej.smart.hotel.controllers;


import com.dynamics.andrzej.smart.hotel.entities.Reservation;
import com.dynamics.andrzej.smart.hotel.models.ClientReservation;
import com.dynamics.andrzej.smart.hotel.models.ReservationRequest;
import com.dynamics.andrzej.smart.hotel.models.ReservationResponse;
import com.dynamics.andrzej.smart.hotel.models.ReservationUpdateRequest;
import com.dynamics.andrzej.smart.hotel.services.ReservationService;
import com.dynamics.andrzej.smart.hotel.services.RoomService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/client/api/reservation")
public class ClientReservationController {
    private final ReservationService reservationService;
    private final RoomService roomService;

    public ClientReservationController(ReservationService reservationService, RoomService roomService) {
        this.reservationService = reservationService;
        this.roomService = roomService;
    }

    @GetMapping("/all")
    public List<Reservation> getAll() {
        return reservationService.findAll();
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return reservationService.delete(id);
    }

    @PostMapping("/{id}")
    public Reservation update(@PathVariable Long id, @RequestBody ReservationUpdateRequest reservationUpdateRequest) {
        return reservationService.update(id, reservationUpdateRequest);
    }

    @PostMapping
    public ReservationResponse reserve(@RequestBody ClientReservation clientReservation) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final UserDetails client = (UserDetails) authentication.getPrincipal();

        final ReservationRequest reservationRequest = new ReservationRequest();
        reservationRequest.setRoomIds(clientReservation.getRoomIds().stream().map(Long::valueOf).collect(Collectors.toList()));
        reservationRequest.setEmail(client.getUsername());
        reservationRequest.setFrom(clientReservation.getFrom());
        reservationRequest.setTo(clientReservation.getTo());
        return roomService.reserveForClient(reservationRequest);
    }
}
