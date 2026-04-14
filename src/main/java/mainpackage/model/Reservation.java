package mainpackage.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record Reservation (
        Long id,
        Long userId,
        Long roomId,
        @JsonFormat(pattern = "dd.MM.yyyy")
        LocalDate startDate,
        @JsonFormat(pattern = "dd.MM.yyyy")
        LocalDate endDate,
        ReservationStatus status
) {
}
