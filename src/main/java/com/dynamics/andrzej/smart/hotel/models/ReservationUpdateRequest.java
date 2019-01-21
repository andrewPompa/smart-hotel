package com.dynamics.andrzej.smart.hotel.models;

import lombok.Data;

import java.util.Date;

@Data
public class ReservationUpdateRequest {
    private Date fromDay;
    private Date toDay;
}
