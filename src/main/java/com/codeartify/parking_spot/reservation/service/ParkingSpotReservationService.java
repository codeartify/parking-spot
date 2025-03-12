package com.codeartify.parking_spot.reservation.service;

import com.codeartify.parking_spot.reservation.dto.ParkingReservationRequest;
import com.codeartify.parking_spot.reservation.dto.ParkingReservationResponse;
import com.codeartify.parking_spot.reservation.model.ParkingReservation;
import com.codeartify.parking_spot.reservation.model.ParkingSpot;
import com.codeartify.parking_spot.reservation.repository.ParkingReservationRepository;
import com.codeartify.parking_spot.reservation.repository.ParkingSpotRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalTime;

@Service
@AllArgsConstructor
public class ParkingSpotReservationService {
    private final ParkingReservationRepository parkingReservationRepository;
    private final ParkingSpotRepository parkingSpotRepository;

    private static final LocalTime OPENING_TIME = LocalTime.of(6, 0); // 6:00 AM
    private static final LocalTime CLOSING_TIME = LocalTime.of(22, 0); // 10:00 PM

    @Transactional
    public ResponseEntity<Object> reserveParkingSpot(ParkingReservationRequest request) {
        // Validate reservation duration
        if (Duration.between(request.getStartTime(), request.getEndTime()).toMinutes() >= 30) {
            // Ensure the end time is after the start time
            if (!request.getEndTime().isBefore(request.getStartTime())) {
                // Ensure reservation is within operating hours
                if (!request.getStartTime().toLocalTime().isBefore(OPENING_TIME) && !request.getEndTime().toLocalTime().isAfter(CLOSING_TIME)) {
                    // Check if the user already has an active reservation
                    boolean hasActiveReservation = parkingReservationRepository
                            .hasActiveReservation(request.getReservedBy(), request.getStartTime(), request.getEndTime());

                    if (!hasActiveReservation) {
                        // Find any available spot
                        ParkingSpot spot = this.parkingSpotRepository.findAnyAvailableSpot();

                        if (spot != null) {
                            // Create and save the reservation
                            ParkingReservation reservation = new ParkingReservation(
                                    request.getReservedBy(),
                                    spot.getId(),
                                    request.getStartTime(),
                                    request.getEndTime());
                            this.parkingReservationRepository.save(reservation);

                            // Mark the parking spot as unavailable
                            spot.setAvailable(false);
                            this.parkingSpotRepository.save(spot);

                            // Build and return the response
                            var response = new ParkingReservationResponse();
                            response.setReservationId(reservation.getId());
                            response.setReservedBy(request.getReservedBy());
                            response.setStartTime(request.getStartTime());
                            response.setEndTime(request.getEndTime());

                            return ResponseEntity.status(HttpStatus.CREATED).body(response);
                        } else {
                            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No available spot left.");
                        }

                    } else {
                        return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body("You already have an active reservation.");
                    }

                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Reservations can only be made between 6:00 AM and 10:00 PM.");
                }

            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("End time must be after start time.");
            }

        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Reservation must be at least 30 minutes long.");
        }

    }
}

