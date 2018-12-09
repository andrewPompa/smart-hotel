package com.dynamics.andrzej.smart.hotel.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Room {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private int size;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private RoomType type;

    @ManyToMany(mappedBy = "rooms")
    private List<Reservation> reservations;
}
