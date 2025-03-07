package com.codeartify.parking_spot.reservation.adapter.data_access;
 
import com.codeartify.examples.parking_spot_reservation.service.port.out.FindAnyAvailableSpot;
import com.codeartify.parking_spot.reservation.model.ParkingSpotDbEntity;
import com.codeartify.parking_spot.reservation.repository.ParkingSpotDbEntityRepository;
import com.codeartify.parking_spot.reservation.service.NoAvailableSpotLeftException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class ParkingSpotRepositoryAdapter implements FindAnyAvailableSpot {
    private final ParkingSpotDbEntityRepository parkingSpotDbEntityRepository;

    @Override
    public Long findAnyAvailableSpot() throws NoAvailableSpotLeftException {
        ParkingSpotDbEntity spot = parkingSpotDbEntityRepository.findAnyAvailableSpot();

        if (spot == null) {
            throw new NoAvailableSpotLeftException();
        }

        return spot.getId();
    }
}
