package ru.blaskowitz.lissatradingmoexapiservice.service.candle;

import ru.blaskowitz.lissatradingmoexapiservice.dto.candle.CandleDto;
import ru.blaskowitz.lissatradingmoexapiservice.dto.candle.CandleIntervalEnum;

import java.util.List;

public interface CandleAggregator {
    public List<CandleDto> aggregate(List<CandleDto> candles, CandleIntervalEnum interval);
}
