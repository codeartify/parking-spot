package com.codeartify.parking_spot.reservation.controller;

import com.codeartify.parking_spot.reservation.dto.ParkingReservationRequest;
import com.codeartify.parking_spot.reservation.model.ParkingSpot;
import com.codeartify.parking_spot.reservation.repository.ParkingReservationRepository;
import com.codeartify.parking_spot.reservation.repository.ParkingSpotRepository;
import com.codeartify.parking_spot.reservation.service.ParkingSpotReservationService;
import org.approvaltests.combinations.CombinationApprovals;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;


class ParkingSpotReservationServiceApprovalTestShould {

    @Test
    void test() {
        CombinationApprovals.verifyAllCombinations(
                ParkingSpotReservationServiceApprovalTestShould::invoke,
                new ParkingReservationRequest[]{
                        new ParkingReservationRequest(),
                        new ParkingReservationRequest("Olly", null, null),
                        new ParkingReservationRequest("Olly", LocalDateTime.of(2020, 1, 1, 1, 1), null),
                        new ParkingReservationRequest("Olly", null, LocalDateTime.of(2020, 1, 1, 8, 1)),
                        new ParkingReservationRequest(null, LocalDateTime.of(2020, 1, 1, 8, 0), LocalDateTime.of(2020, 1, 1, 8, 1)),
                        new ParkingReservationRequest("Olly", LocalDateTime.of(2020, 1, 1, 19, 5), LocalDateTime.of(2020, 1, 1, 8, 10)),
                        new ParkingReservationRequest("Olly", LocalDateTime.of(2020, 1, 1, 8, 0), LocalDateTime.of(2020, 1, 1, 8, 1)),
                        new ParkingReservationRequest("Olly", LocalDateTime.of(2020, 1, 1, 19, 0), LocalDateTime.of(2020, 1, 1, 22, 1)),
                        new ParkingReservationRequest("Olly", LocalDateTime.of(2020, 1, 1, 8, 0), LocalDateTime.of(2020, 1, 1, 8, 30)),
                        new ParkingReservationRequest("Olly", LocalDateTime.of(2020, 1, 1, 8, 0), LocalDateTime.of(2020, 1, 1, 8, 31)),
                },
                new ParkingReservationRepository[]{withActiveReservation(), withoutActiveReservation()},
                new ParkingSpotRepository[]{findNoSpot(), findSpot()}
        );
    }

    private ParkingSpotRepository findSpot() {
        var mock = mock(ParkingSpotRepository.class);
        when(mock.findAnyAvailableSpot()).thenReturn(new ParkingSpot(true));
        when(mock.toString()).thenReturn("");
        return mock;
    }

    private static ParkingSpotRepository findNoSpot() {
        var mock = mock(ParkingSpotRepository.class);
        when(mock.findAnyAvailableSpot()).thenReturn(null);
        when(mock.toString()).thenReturn("");
        return mock;
    }

    private static ParkingReservationRepository withoutActiveReservation() {
        var mock = mock(ParkingReservationRepository.class);
        when(mock.hasActiveReservation(any(), any(), any())).thenReturn(false);
        when(mock.toString()).thenReturn("");
        return mock;
    }

    private static ParkingReservationRepository withActiveReservation() {
        var mock = mock(ParkingReservationRepository.class);
        when(mock.hasActiveReservation(any(), any(), any())).thenReturn(true);
        when(mock.toString()).thenReturn("");
        return mock;
    }

    private static String invoke(ParkingReservationRequest request, ParkingReservationRepository parkingReservationRepository, ParkingSpotRepository parkingSpotRepository) {
        var service = new ParkingSpotReservationService(parkingReservationRepository, parkingSpotRepository);
        var controller = new ParkingSpotReservationController(service);
        var response = controller.reserveParkingSpot(request);
        return stateAsString(response);
    }


    private static String stateAsString(ResponseEntity<Object> result) {
        return "Body: " + result.getBody() + " status code: " + result.getStatusCode();
    }
}
