package com.codeartify.parking_spot.reservation.dto;

import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ParkingReservationRequest {
    private String reservedBy;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
