package mainpackage.exception;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

//обработчик ошибок
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(Exception e) {
        log.error("Handle exception {} ", e.getMessage());
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO("internal server error", e.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponseDTO);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleEntityNotFound(EntityNotFoundException e) {
        log.error("Entity not found {} ", e.getMessage());
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO("Entity not found", e.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDTO);
    }

    @ExceptionHandler(exception = { IllegalArgumentException.class, IllegalStateException.class, MethodArgumentNotValidException.class } )
    public ResponseEntity<ErrorResponseDTO> handleBadRequset(Exception e) {
        log.error("Illegal argument/state {} ", e.getMessage());
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO("bad request", e.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDTO);
    }

}
