package com.codeartify.parking_spot.reservation.service;

import com.codeartify.examples.parking_spot_reservation.service.ReservationPeriod;
import com.codeartify.parking_spot.reservation.adapter.data_access.ParkingReservationRepositoryAdapter;
import com.codeartify.parking_spot.reservation.adapter.data_access.ParkingSpotRepositoryAdapter;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class ParkingSpotReservationService {
    private final ParkingReservationRepositoryAdapter parkingReservationRepository;
    private final ParkingSpotRepositoryAdapter parkingSpotRepository;


    @Transactional
    public ParkingReservationResult reserveParkingSpot(LocalDateTime startTime, LocalDateTime endTime, String reservingMember) {

        // Check if the user already has an active reservation
        var reservationPeriod = ReservationPeriod.create(startTime, endTime);
        var hasActiveReservation = parkingReservationRepository.hasActiveReservation(reservationPeriod, reservingMember);

        if (hasActiveReservation) {
            throw new ActiveReservationException();
        }

        // Find any available spot
        var parkingSpotId = parkingSpotRepository.findAnyAvailableSpot();

        var parkingReservation = new ParkingReservation(parkingSpotId, reservingMember, reservationPeriod);

        var storedReservation = parkingReservationRepository.storeParkingReservation(parkingReservation);

        // Build and return the response
        return new ParkingReservationResult(storedReservation.id(),
                storedReservation.reservingMember(),
                storedReservation.startTime(),
                storedReservation.endTime());
    }

}

