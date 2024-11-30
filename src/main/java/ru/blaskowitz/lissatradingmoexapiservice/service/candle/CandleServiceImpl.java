package ru.blaskowitz.lissatradingmoexapiservice.service.candle;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.blaskowitz.lissatradingmoexapiservice.client.IssMoexClient;
import ru.blaskowitz.lissatradingmoexapiservice.dto.candle.CandleDto;
import ru.blaskowitz.lissatradingmoexapiservice.dto.candle.CandleListDto;
import ru.blaskowitz.lissatradingmoexapiservice.dto.candle.CandleIntervalEnum;
import ru.blaskowitz.lissatradingmoexapiservice.dto.candle.CandlesRequestDto;
import ru.blaskowitz.lissatradingmoexapiservice.dto.moex.Content;
import ru.blaskowitz.lissatradingmoexapiservice.dto.moex.ContentValidator;
import ru.blaskowitz.lissatradingmoexapiservice.service.ContentMapper;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static ru.blaskowitz.lissatradingmoexapiservice.service.ServiceConstants.MAX_CANDLES_PER_REQUEST;
import static ru.blaskowitz.lissatradingmoexapiservice.service.ServiceConstants.MOEX_CANDLES_TABLE_NAME;

@Service
@RequiredArgsConstructor
@Slf4j
public class CandleServiceImpl implements CandleService {

    private final IssMoexClient issMoexClient;
    private final ContentMapper contentMapper;
    private final ContentValidator contentValidator;

    @Override
    public CandleListDto getCandleList(CandlesRequestDto candlesRequestDto) {

        int startParameter = MAX_CANDLES_PER_REQUEST;
        Content allCandles = issMoexClient
                .getCandleList(candlesRequestDto, 0)
                .block()
                .getContentMap()
                .get(MOEX_CANDLES_TABLE_NAME);
        contentValidator.validateWithSecId(allCandles, candlesRequestDto.getInstrumentId());
        Content additionalCandles;

        while (!((additionalCandles = issMoexClient
                .getCandleList(candlesRequestDto, startParameter)
                .block()
                .getContentMap()
                .get(MOEX_CANDLES_TABLE_NAME))
                .getData()
                .isEmpty()))
        {
            allCandles.getData().addAll(additionalCandles.getData());
            startParameter += MAX_CANDLES_PER_REQUEST;
        }

        List<CandleDto> candleDtoList = contentMapper
                .ContentToPojoListUsingColumns(allCandles, CandleDto.class);

        if (candlesRequestDto.getInterval().isMoexSupported()) {
            setIsCompleteField(candleDtoList, candlesRequestDto.getInterval());
            return new CandleListDto(candleDtoList);
        }
        return new CandleListDto(aggregateCandles(candleDtoList,
                candlesRequestDto.getInterval()));
    }

    private List<CandleDto> aggregateCandles(List<CandleDto> candleDtoList,
                                         CandleIntervalEnum candleIntervalEnum) {
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
            outputCandles.add(new CandleDto(volume, open, close, high, low, begin, null));
        }
        setIsCompleteField(outputCandles, candleIntervalEnum);
        return outputCandles;
    }

    private void setIsCompleteField(List<CandleDto> candles, CandleIntervalEnum candleIntervalEnum) {
        candles.forEach(candleDto -> {
            candleDto.setIsComplete(OffsetDateTime
                    .now()
                    .isAfter(candleDto.getBegin().plusMinutes(candleIntervalEnum.getValue())));
        });
    }
}
