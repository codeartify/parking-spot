package com.codeartify.parking_spot.reservation.controller;

import com.codeartify.parking_spot.reservation.adapter.data_access.ParkingReservationRepositoryAdapter;
import com.codeartify.parking_spot.reservation.adapter.data_access.ParkingSpotRepositoryAdapter;
import com.codeartify.parking_spot.reservation.dto.ParkingReservationRequest;
import com.codeartify.parking_spot.reservation.model.ParkingSpotDbEntity;
import com.codeartify.parking_spot.reservation.repository.ParkingReservationDbEntityRepository;
import com.codeartify.parking_spot.reservation.repository.ParkingSpotDbEntityRepository;
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
                ParkingSpotReservationServiceApprovalTestShould::method,
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
                new ParkingReservationDbEntityRepository[]{withActiveReservation(), withoutActiveReservation()},
                new ParkingSpotDbEntityRepository[]{findNoSpot(), findSpot()}
        );
    }

    private ParkingSpotDbEntityRepository findSpot() {
        var mock = mock(ParkingSpotDbEntityRepository.class);
        when(mock.findAnyAvailableSpot()).thenReturn(new ParkingSpotDbEntity(true));
        when(mock.toString()).thenReturn("");
        return mock;
    }

    private static ParkingSpotDbEntityRepository findNoSpot() {
        var mock = mock(ParkingSpotDbEntityRepository.class);
        when(mock.findAnyAvailableSpot()).thenReturn(null);
        when(mock.toString()).thenReturn("");
        return mock;
    }

    private static ParkingReservationDbEntityRepository withoutActiveReservation() {
        var mock = mock(ParkingReservationDbEntityRepository.class);
        when(mock.hasActiveReservation(any(), any(), any())).thenReturn(false);
        when(mock.toString()).thenReturn("");
        return mock;
    }

    private static ParkingReservationDbEntityRepository withActiveReservation() {
        var mock = mock(ParkingReservationDbEntityRepository.class);
        when(mock.hasActiveReservation(any(), any(), any())).thenReturn(true);
        when(mock.toString()).thenReturn("");
        return mock;
    }

    private static String method(ParkingReservationRequest request, ParkingReservationDbEntityRepository parkingReservationRepository, ParkingSpotDbEntityRepository parkingSpotRepository) {
        var parkingReservationRepositoryAdapter = new ParkingReservationRepositoryAdapter(parkingReservationRepository, parkingSpotRepository);
        var parkingSpotRepositoryAdapter = new ParkingSpotRepositoryAdapter(parkingSpotRepository);
        var service = new ParkingSpotReservationService(parkingReservationRepositoryAdapter, parkingSpotRepositoryAdapter);
        var controller = new ParkingSpotReservationController(service);
        var result = controller.reserveParkingSpot(request);
        
        return stateAsString(result);
    }


    private static String stateAsString(ResponseEntity<Object> result) {
        return "Body: " + result.getBody() + " status code: " + result.getStatusCode();
    }
}
