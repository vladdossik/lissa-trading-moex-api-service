package ru.blaskowitz.lissatradingmoexapiservice.dto.candle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandlesRequestDto {

    private String instrumentId;
    private OffsetDateTime from;
    private OffsetDateTime till;
    private CandleIntervalEnum interval;

}
