package lissa.trading.moexapi.service.service.candle;

import lissa.trading.moexapi.service.dto.candle.CandleDto;
import lissa.trading.moexapi.service.dto.candle.CandleIntervalEnum;

import java.util.List;

public interface CandleAggregator {
    List<CandleDto> aggregateCandles(List<CandleDto> candles, CandleIntervalEnum interval);
}
