package com.dynamics.andrzej.smart.hotel.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Reservation {

    @Id
    @Column(nullable = false, unique = true)
    private long reservationCode;

    @ManyToMany
    @JoinColumn(name = "client", nullable = false)
    private List<Client> client;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "room", nullable = false)
    private List<Room> room;

    @Column(nullable = false)
    private double roomPrice;

    @Column(nullable = false)
    private Date fromDay;

    @Column(nullable = false)
    private Date toDay;

    @ManyToMany
    @JoinColumn(name = "receptionist")
    private List<Receptionist> receptionist;
}
