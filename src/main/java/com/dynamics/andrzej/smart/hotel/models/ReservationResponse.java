package com.dynamics.andrzej.smart.hotel.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReservationResponse {
    private boolean isNewClient;
    private String code;
}
