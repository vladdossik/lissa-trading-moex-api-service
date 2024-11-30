package ru.blaskowitz.lissatradingmoexapiservice.service.candle;

import org.springframework.stereotype.Component;
import ru.blaskowitz.lissatradingmoexapiservice.dto.candle.CandleDto;
import ru.blaskowitz.lissatradingmoexapiservice.dto.candle.CandleIntervalEnum;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class CandleAggregatorImpl implements CandleAggregator {

    public List<CandleDto> aggregate(List<CandleDto> candleDtoList,
                                             CandleIntervalEnum candleIntervalEnum) {
        if (candleIntervalEnum.isMoexSupported()) {
            candleDtoList.stream().forEach(candleDto -> {
                setIsCompleteField(candleDto, candleIntervalEnum);
            });
            return candleDtoList;
        }

        List<CandleDto> outputCandles = new ArrayList<>();

        for (int i = 0; i < candleDtoList.size(); i++) {
            CandleDto startCandle = candleDtoList.get(i);
            OffsetDateTime groupStart = startCandle.getBegin();
            OffsetDateTime groupEnd = groupStart.plusMinutes(candleIntervalEnum.getValue() - 1);
            List<CandleDto> candleGroup = new ArrayList<>();

            while (i < candleDtoList.size()
                    && !candleDtoList.get(i).getBegin().isAfter(groupEnd)) {
                candleGroup.add(candleDtoList.get(i));
                i++;
            }
            i--;

            Double open = candleGroup.get(0).getOpen();
            Double close = candleGroup.get(candleGroup.size() - 1).getClose();
            Double high = candleGroup.stream().mapToDouble(CandleDto::getHigh).max().getAsDouble();
            Double low = candleGroup.stream().mapToDouble(CandleDto::getLow).min().getAsDouble();
            Long volume = candleGroup.stream().mapToLong(CandleDto::getVolume).sum();
            OffsetDateTime begin = candleGroup.get(0).getBegin();
            CandleDto aggregatedCandle = new CandleDto(volume, open, close, high, low, begin, null);
            setIsCompleteField(aggregatedCandle, candleIntervalEnum);
            outputCandles.add(aggregatedCandle);
        }
        return outputCandles;
    }

    private void setIsCompleteField(CandleDto candle, CandleIntervalEnum candleIntervalEnum) {
        candle.setIsComplete(OffsetDateTime
                .now()
                .isAfter(candle.getBegin().plusMinutes(candleIntervalEnum.getValue())));
    }
}
