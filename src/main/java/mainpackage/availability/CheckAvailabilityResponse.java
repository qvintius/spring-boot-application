package mainpackage.availability;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckAvailabilityResponse {

    private String message;
    private AvailabilityStatus status;

}
