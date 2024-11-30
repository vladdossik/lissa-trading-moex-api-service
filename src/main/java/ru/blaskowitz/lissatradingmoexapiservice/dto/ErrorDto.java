package ru.blaskowitz.lissatradingmoexapiservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ErrorDto {
    private String error;
    private LocalDateTime timestamp;
    private int status;

    public ErrorDto(String error, int status) {
        this.error = error;
        this.timestamp = LocalDateTime.now();
        this.status = status;
    }
}
