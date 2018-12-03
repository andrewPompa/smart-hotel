package com.dynamics.andrzej.smart.hotel.models;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ClientReservation {
    private List<String> roomIds;
    private Date from;
    private Date to;
}
