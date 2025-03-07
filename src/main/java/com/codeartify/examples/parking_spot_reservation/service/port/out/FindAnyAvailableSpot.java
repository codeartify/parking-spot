package com.codeartify.examples.parking_spot_reservation.service.port.out;


import com.codeartify.parking_spot.reservation.service.NoAvailableSpotLeftException;

public interface FindAnyAvailableSpot {
    Long findAnyAvailableSpot() throws NoAvailableSpotLeftException;
}
