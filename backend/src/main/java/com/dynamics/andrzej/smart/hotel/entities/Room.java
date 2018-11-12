package com.dynamics.andrzej.smart.hotel.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Room {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private int size;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private RoomType type;
}
