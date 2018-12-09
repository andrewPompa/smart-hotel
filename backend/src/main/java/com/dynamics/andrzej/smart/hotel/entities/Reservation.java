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
    @GeneratedValue
    private long id;

    @ManyToOne
    @JoinColumn(name = "client", nullable = false)
    private Client client;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "room", nullable = false)
    private List<Room> rooms;

    @Column(nullable = false)
    private double roomPrice;

    @Column(nullable = false)
    private Date fromDay;

    @Column(nullable = false)
    private Date toDay;

    @ManyToOne
    @JoinColumn(name = "receptionist")
    private Receptionist receptionist;
}
