package ru.blaskowitz.lissatradingmoexapiservice.service.candle;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.blaskowitz.lissatradingmoexapiservice.client.IssMoexClient;
import ru.blaskowitz.lissatradingmoexapiservice.dto.candle.CandleDto;
import ru.blaskowitz.lissatradingmoexapiservice.dto.candle.CandleListDto;
import ru.blaskowitz.lissatradingmoexapiservice.dto.candle.CandlesRequestDto;
import ru.blaskowitz.lissatradingmoexapiservice.dto.moex.Content;
import ru.blaskowitz.lissatradingmoexapiservice.dto.moex.ContentValidator;
import ru.blaskowitz.lissatradingmoexapiservice.service.ContentMapper;

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
    private final CandleAggregator candleAggregator;

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

       return new CandleListDto(candleAggregator.aggregate(candleDtoList, candlesRequestDto.getInterval()));
    }
}
