package com.codeartify.parking_spot.reservation.service;

import java.time.LocalDateTime;

public record ParkingReservationResult(Long id, String reservingMember, LocalDateTime startTime, LocalDateTime endTime) {
}
