package com.codeartify.parking_spot.reservation.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

class ParkingSpotReservationControllerApprovalTestShould {
    @Test
    void cover_reserve_parking_spot() {
    }

    private String stateAsString(ResponseEntity<Object> result) {
        return "Body: " + result.getBody() + " status code: " + result.getStatusCode();
    }

}
