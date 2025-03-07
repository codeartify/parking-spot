package com.codeartify.parking_spot.reservation.adapter.data_access;

import com.codeartify.examples.parking_spot_reservation.service.ReservationPeriod;
import com.codeartify.parking_spot.reservation.model.ParkingReservationDbEntity;
import com.codeartify.parking_spot.reservation.repository.ParkingReservationDbEntityRepository;
import com.codeartify.parking_spot.reservation.repository.ParkingSpotDbEntityRepository;
import com.codeartify.parking_spot.reservation.service.ParkingReservation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class ParkingReservationRepositoryAdapter {
    private final ParkingReservationDbEntityRepository parkingReservationDbEntityRepository;
    private final ParkingSpotDbEntityRepository parkingSpotDbEntityRepository;

    public boolean hasActiveReservation(ReservationPeriod reservationPeriod, String reservingMember) {
        return this.parkingReservationDbEntityRepository.hasActiveReservation(reservingMember, reservationPeriod.getStartTime(), reservationPeriod.getEndTime());
    }

    public ParkingReservation storeParkingReservation(ParkingReservation parkingReservation) {
        // Create and save the reservation
        ParkingReservationDbEntity reservationDbEntity = new ParkingReservationDbEntity(
                parkingReservation.reservingMember(),
                parkingReservation.parkingSpotId(),
                parkingReservation.startTime(),
                parkingReservation.endTime());
        this.parkingReservationDbEntityRepository.save(reservationDbEntity);

        this.parkingSpotDbEntityRepository.findById(parkingReservation.parkingSpotId())
                .ifPresent(parkingSpotDbEntity -> {
                    // Mark the parking spot as unavailable
                    parkingSpotDbEntity.setAvailable(false);
                    this.parkingSpotDbEntityRepository.save(parkingSpotDbEntity);
                });


        var reservationPeriod = ReservationPeriod.create(reservationDbEntity.getStartTime(), reservationDbEntity.getEndTime());

        return new ParkingReservation(
            reservationDbEntity.getSpotId(),
            reservationDbEntity.getReservedBy(),
            reservationPeriod);
    }
}
