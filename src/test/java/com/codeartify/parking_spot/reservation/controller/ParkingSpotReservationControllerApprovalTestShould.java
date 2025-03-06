package com.codeartify.parking_spot.reservation.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.fail;

class ParkingSpotReservationControllerApprovalTestShould {
    @Test
    void cover_reserve_parking_spot() {
        fail();
    }

    private String stateAsString(ResponseEntity<Object> result) {
        return "Body: " + result.getBody() + " status code: " + result.getStatusCode();
    }

}
