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
    private Long id;
    @NotNull
    private Long roomId;
    @NotNull
    private LocalDate startDate;
    @NotNull
    private LocalDate endDate;

}
