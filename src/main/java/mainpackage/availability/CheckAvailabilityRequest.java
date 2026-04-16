package mainpackage.availability;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckAvailabilityRequest {
    @NotNull
    Long roomId;
    @NotNull
    LocalDate startDate;
    @NotNull
    LocalDate endDate;


}
