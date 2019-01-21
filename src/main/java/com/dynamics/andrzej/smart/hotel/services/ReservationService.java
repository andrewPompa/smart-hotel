package com.dynamics.andrzej.smart.hotel.services;

import com.dynamics.andrzej.smart.hotel.entities.Reservation;
import com.dynamics.andrzej.smart.hotel.entities.Room;
import com.dynamics.andrzej.smart.hotel.models.ReservationUpdateRequest;
import com.dynamics.andrzej.smart.hotel.respositories.ReservationRepository;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

@Service
public class ReservationService {
    private final ClientService clientService;
    private final ReservationRepository reservationRepository;

    public ReservationService(ClientService clientService, ReservationRepository reservationRepository) {
        this.clientService = clientService;
        this.reservationRepository = reservationRepository;
    }

    public List<Reservation> findAll() {
        List<Reservation> reservations = reservationRepository.findAll();
        reservations.forEach(reservation -> reservation.getRooms().forEach(room -> room.setReservations(Collections.emptyList())));
        return reservations;
    }

    public Reservation update(Long id, ReservationUpdateRequest request) {
        final Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Cannot find room"));
        for (Room room : reservation.getRooms()) {
            final List<Reservation> ifRoomAlreadyReserved = reservationRepository.findIfRoomAlreadyReserved(
                    new Date(request.getFromDay().getTime()),
                    new Date(request.getToDay().getTime()),
                    room.getId(),
                    id
            );
            if (!ifRoomAlreadyReserved.isEmpty()) {
                throw new IllegalArgumentException("room already reserved");
            }
        }
        reservation.setFromDay(new Date(request.getFromDay().getTime()));
        reservation.setToDay(new Date(request.getToDay().getTime()));
        return reservationRepository.save(reservation);
    }

    public boolean delete(Long id) {
        final Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Cannot find reservation"));
        final Date fromDay = reservation.getFromDay();
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 5);
        clientService.subtractDebt(reservation.getClient(), reservation.getRoomPrice());
        if (fromDay.after(new Date(calendar.getTimeInMillis()))) {
            double reservationFee = reservation.getRoomPrice() * 0.5;
            clientService.addDebtToClient(reservation.getClient(), reservationFee);
        }
        reservationRepository.deleteById(id);
        return true;
    }
}
