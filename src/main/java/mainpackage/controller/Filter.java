package mainpackage.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Filter {

        private Long roomId;
        private Long userId;
        private Integer pageSize;
        private Integer pageNumber;
}
