package mainpackage.availability;

import mainpackage.model.ReservationStatus;
import mainpackage.repository.ReservationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationAvailabilityService {
    private static final Logger log = LoggerFactory.getLogger(ReservationAvailabilityService.class);
    private final ReservationRepository reservationRepository;

    public ReservationAvailabilityService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public Boolean crosWithOtherReservations(Long id, Long roomId, LocalDate startDate, LocalDate endDate, ReservationStatus status) {
        if (startDate.isBefore(endDate))
            throw new IllegalArgumentException("Start date must be before end date");
        //совпадения с другими комнатами
        List<Long> conflictReservationIds = reservationRepository.findAllConflictReservationIds(id, roomId, startDate, endDate, status);
        if (conflictReservationIds.isEmpty())
            return false;
        log.info("Conflicting with ids {}", conflictReservationIds);
        return true;
    }

}
