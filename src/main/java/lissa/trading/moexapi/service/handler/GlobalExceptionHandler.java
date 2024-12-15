package lissa.trading.moexapi.service.handler;

import lissa.trading.moexapi.service.exception.IllegalCandlesRequestTimeIntervalException;
import lissa.trading.moexapi.service.exception.IllegalCandlesRequestZoneOffsetException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import lissa.trading.moexapi.service.dto.ErrorDto;
import lissa.trading.moexapi.service.exception.NotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorDto> handleNotFoundException(NotFoundException exception) {
        return new ResponseEntity<>(
                new ErrorDto(exception.getMessage(),
                HttpStatus.NOT_FOUND.value()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalCandlesRequestTimeIntervalException.class)
    public ResponseEntity<ErrorDto> handleIllegalCandlesRequestTimeIntervalException(
            IllegalCandlesRequestTimeIntervalException exception) {
        return new ResponseEntity<>(new ErrorDto(exception.getMessage(),
                HttpStatus.BAD_REQUEST.value()),
                HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<ErrorDto> handleWebClientResponseException(WebClientResponseException exception) {
        return new ResponseEntity<>(
                new ErrorDto(exception.getMessage(),
                exception.getStatusCode().value()),
                exception.getStatusCode());
    }
}
