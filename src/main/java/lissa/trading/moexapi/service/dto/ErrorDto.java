package lissa.trading.moexapi.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
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
