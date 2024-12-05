package lissa.trading.moexapi.service.service.candle;

import org.springframework.stereotype.Component;
import lissa.trading.moexapi.service.dto.candle.CandleDto;
import lissa.trading.moexapi.service.dto.candle.CandleIntervalEnum;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class CandleAggregatorImpl implements CandleAggregator {

    @Override
    public List<CandleDto> aggregateCandles(List<CandleDto> candleDtoList,
                                            CandleIntervalEnum candleIntervalEnum) {
        if (candleIntervalEnum.isMoexSupported()) {
            candleDtoList.stream()
                    .forEach(candleDto -> candleDto
                    .setIsComplete(isComplete(candleDto.getBegin(), candleIntervalEnum)));
            return candleDtoList;
        }

        return groupCandles(candleDtoList, candleIntervalEnum)
                .stream()
                .map(candleGroup -> aggregateCandleGroup(candleGroup, candleIntervalEnum))
                .toList();
    }

    private List<List<CandleDto>> groupCandles(List<CandleDto> candleDtoList,
                                               CandleIntervalEnum candleIntervalEnum) {
        List<List<CandleDto>> candleGroups = new ArrayList<>();

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
            candleGroups.add(candleGroup);
            i--;
        }
        return candleGroups;
    }

    private CandleDto aggregateCandleGroup(List<CandleDto> candleGroup,
                                         CandleIntervalEnum candleIntervalEnum) {
        Double open = candleGroup.get(0).getOpen();
        Double close = candleGroup.get(candleGroup.size() - 1).getClose();
        Double high = candleGroup.stream().mapToDouble(CandleDto::getHigh).max().getAsDouble();
        Double low = candleGroup.stream().mapToDouble(CandleDto::getLow).min().getAsDouble();
        Long volume = candleGroup.stream().mapToLong(CandleDto::getVolume).sum();
        OffsetDateTime begin = candleGroup.get(0).getBegin();
        CandleDto processedCandle = new CandleDto(
                volume, open,
                close, high,
                low, isComplete(begin, candleIntervalEnum),
                begin);

        return processedCandle;
    }

    private boolean isComplete(OffsetDateTime begin, CandleIntervalEnum candleIntervalEnum) {
        return OffsetDateTime.now().isAfter(begin.plusMinutes(candleIntervalEnum.getValue()));
    }

}
