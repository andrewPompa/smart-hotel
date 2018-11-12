package com.dynamics.andrzej.smart.hotel.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Reservation {

    @Id
    @Column(nullable = false, unique = true)
    private long reservationCode;

    @Column(nullable = false)
    private Client client;

    @ManyToMany(mappedBy = "room", fetch = FetchType.EAGER)
    private Room room;

    @Column(nullable = false)
    private double roomPrice;

    @Column(nullable = false)
    private Date from;

    @Column(nullable = false)
    private Date to;

    @ManyToMany(mappedBy = "receptionist", fetch = FetchType.EAGER)
    private Receptionist receptionist;
}
