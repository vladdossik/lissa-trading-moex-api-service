package ru.blaskowitz.lissatradingmoexapiservice.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.blaskowitz.lissatradingmoexapiservice.dto.ErrorDto;
import ru.blaskowitz.lissatradingmoexapiservice.exception.NotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorDto> handleNotFoundException(NotFoundException exception) {
        return new ResponseEntity<>(
                new ErrorDto(exception.getMessage(),
                HttpStatus.NOT_FOUND.value()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<ErrorDto> handleWebClientResponseException(WebClientResponseException exception) {
        return new ResponseEntity<>(
                new ErrorDto(exception.getMessage(),
                exception.getStatusCode().value()),
                exception.getStatusCode());

    }
}
