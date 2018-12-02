package com.dynamics.andrzej.smart.hotel.controllers;


import com.dynamics.andrzej.smart.hotel.entities.Reservation;
import com.dynamics.andrzej.smart.hotel.models.ReservationUpdateRequest;
import com.dynamics.andrzej.smart.hotel.services.ReservationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/api/reservation")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
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
}
