package mainpackage.availability;

import jakarta.validation.Valid;
import mainpackage.model.ReservationStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservation/availability")
public class ReservationAvailabilityController {

    private static final Logger log = LoggerFactory.getLogger(ReservationAvailabilityController.class);

    private final ReservationAvailabilityService reservationAvailabilityService;

    public ReservationAvailabilityController(ReservationAvailabilityService reservationAvailabilityService) {
        this.reservationAvailabilityService = reservationAvailabilityService;
    }

    @PostMapping("/check")
    public ResponseEntity<CheckAvailabilityResponse> checkAvailability(@Valid @RequestBody CheckAvailabilityRequest request) {
        log.info("Check availability request: {}", request);
        Boolean isCrossWithOthers = reservationAvailabilityService.crosWithOtherReservations(
                request.getId(), request.getRoomId(), request.getStartDate(), request.getEndDate(), ReservationStatus.PENDING);
        if (isCrossWithOthers) {
            return ResponseEntity.status(HttpStatus.OK).body(new CheckAvailabilityResponse("Room is not available to reservation", AvailabilityStatus.RESERVED));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new CheckAvailabilityResponse("Room is not available to reservation", AvailabilityStatus.AVAILABLE));
    }


}
