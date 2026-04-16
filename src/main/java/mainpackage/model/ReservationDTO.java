package mainpackage.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {
        @Null
        private Long id;
        @NotNull
        private Long userId;
        @NotNull
        private Long roomId;
        @NotNull
        @FutureOrPresent
        @JsonFormat(pattern = "dd.MM.yyyy")
        private LocalDate startDate;
        @NotNull
        @FutureOrPresent
        @JsonFormat(pattern = "dd.MM.yyyy")
        private LocalDate endDate;
        private ReservationStatus status;

}
