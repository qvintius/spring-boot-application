package mainpackage.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {
        private Long id;
        private Long userId;
        private Long roomId;
        @JsonFormat(pattern = "dd.MM.yyyy")
        private LocalDate startDate;
        @JsonFormat(pattern = "dd.MM.yyyy")
        private LocalDate endDate;
        private ReservationStatus status;

}
