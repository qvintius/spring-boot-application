package mainpackage.controller;

import mainpackage.model.ReservationDTO;
import mainpackage.service.ReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

    private static final Logger logger = LoggerFactory.getLogger(ReservationController.class);
    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationDTO> getReservationById(@PathVariable Long id) {
        logger.info("getReservationById");
        try{
            return ResponseEntity.status(HttpStatus.OK).body(reservationService.getReservationById(id));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("")
    public ResponseEntity<List<ReservationDTO>> getAllReservations() {
        return ResponseEntity.ok(reservationService.getAllReservations());
    }

    @PostMapping("")
    public ResponseEntity<ReservationDTO> createReservation(@RequestBody ReservationDTO reservationDTOToCreate) {
        logger.info("createReservation");
        ReservationDTO reservationDTO = reservationService.createReservation(reservationDTOToCreate);
        return ResponseEntity.status(HttpStatus.CREATED).header("test-header", "123").body(reservationDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservationDTO> updateReservation(@PathVariable Long id, @RequestBody ReservationDTO reservationDTOToUpdate) {
        logger.info("updateReservation");
        ReservationDTO reservationDTO = reservationService.updateReservation(id, reservationDTOToUpdate);
        try {
            return ResponseEntity.status(HttpStatus.OK).body(reservationDTO);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        logger.info("deleteReservation");
        try {
            reservationService.deleteReservation(id);
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<ReservationDTO> approveReservation(@PathVariable Long id) {
        logger.info("approveReservation {}", id);//подтверждение бронирования
        ReservationDTO reservationDTO = reservationService.approveReservation(id);
        return ResponseEntity.status(HttpStatus.OK).body(reservationDTO);
    }
}
