package mainpackage.availability;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckAvailabilityResponse {
    String message;
    AvailabilityStatus status;

}
