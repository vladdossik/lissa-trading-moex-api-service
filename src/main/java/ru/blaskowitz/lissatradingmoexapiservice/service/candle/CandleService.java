package ru.blaskowitz.lissatradingmoexapiservice.service.candle;

import ru.blaskowitz.lissatradingmoexapiservice.dto.candle.CandleListDto;
import ru.blaskowitz.lissatradingmoexapiservice.dto.candle.CandlesRequestDto;

public interface CandleService {
    CandleListDto getCandleList(CandlesRequestDto candlesRequestDto);
}
