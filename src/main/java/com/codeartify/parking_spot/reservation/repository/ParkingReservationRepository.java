package com.codeartify.parking_spot.reservation.repository;

import com.codeartify.parking_spot.reservation.model.ParkingReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ParkingReservationRepository extends JpaRepository<ParkingReservation, Long> {
    @Query("SELECT COUNT(r) > 0 FROM ParkingReservation r " +
            "WHERE r.reservedBy = :reservedBy " +
            "AND r.startTime < :endTime " +
            "AND r.endTime > :startTime")
    boolean hasActiveReservation(@Param("reservedBy") String reservedBy,
                                 @Param("startTime") LocalDateTime startTime,
                                 @Param("endTime") LocalDateTime endTime);
}


