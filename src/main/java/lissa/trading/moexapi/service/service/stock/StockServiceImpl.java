package lissa.trading.moexapi.service.service.stock;

import lissa.trading.moexapi.service.dto.candle.CandleDto;
import lissa.trading.moexapi.service.dto.candle.CandleListDto;
import lissa.trading.moexapi.service.dto.candle.CandlesRequestDto;
import lissa.trading.moexapi.service.dto.stock.CurrencyEnum;
import lissa.trading.moexapi.service.service.ContentMapper;
import lissa.trading.moexapi.service.service.candle.CandleAggregator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import lissa.trading.moexapi.service.client.IssMoexClient;
import lissa.trading.moexapi.service.dto.company.CompanyListDto;
import lissa.trading.moexapi.service.dto.moex.ContentDto;
import lissa.trading.moexapi.service.dto.moex.ContentValidator;
import lissa.trading.moexapi.service.dto.ticker.TickerListDto;
import lissa.trading.moexapi.service.dto.stock.StockDto;
import lissa.trading.moexapi.service.dto.stock.StockDtoList;
import lissa.trading.moexapi.service.dto.stock.StockPriceDto;
import lissa.trading.moexapi.service.dto.stock.StockPriceDtoList;
import lissa.trading.moexapi.service.dto.candle.CandleRequestValidator;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockServiceImpl implements StockService {

    private final IssMoexClient issMoexClient;
    private final ContentMapper contentMapper;
    private final ContentValidator contentValidator;
    private final CandleAggregator candleAggregator;
    private final CandleRequestValidator candleRequestValidator;

    public final static String MOEX_STOCK_INFO_TABLE_NAME = "description";
    public final static String MOEX_STOCK_PRICES_TABLE_NAME = "marketdata";
    public final static String MOEX_CANDLES_TABLE_NAME = "candles";
    public final static int MAX_CANDLES_PER_REQUEST = 500;

    @Override
    public StockDto getStock(String ticker) {
        ContentDto contentDto = issMoexClient
                .getStockByTicker(ticker)
                .block()
                .getContentMap()
                .get(MOEX_STOCK_INFO_TABLE_NAME);

        contentValidator.validateRequestWithTicker(contentDto, ticker);
        StockDto stockDto = contentMapper.contentToPojo(contentDto, StockDto.class);
        stockDto.setFaceunit(CurrencyEnum.mapLegacyToModern(stockDto.getFaceunit()));
        return stockDto;
    }

    @Override
    public StockDtoList getStocks(TickerListDto tickerListDto) {
        List<StockDto> stockDtoList = tickerListDto
                .getTickerList()
                .stream()
                .map(this::getStock)
                .toList();
        return new StockDtoList(stockDtoList);
    }

    @Override
    public StockPriceDtoList getStockPrices(TickerListDto tickerListDto) {
        List<StockPriceDto> stockPriceDtoList = tickerListDto
                .getTickerList()
                .stream()
                .map(ticker -> {
                    ContentDto contentDto = issMoexClient
                            .getStockPriceByTicker(ticker)
                            .block()
                            .getContentMap()
                            .get(MOEX_STOCK_PRICES_TABLE_NAME);
                    contentValidator.validateRequestWithTicker(contentDto, ticker);
                    return contentMapper.contentToPojoUsingColumns(contentDto, StockPriceDto.class);
                })
                .toList();
        
        return new StockPriceDtoList(stockPriceDtoList);
    }

    @Override
    public CompanyListDto getCompanyNames(TickerListDto tickerListDto) {
        List<String> companyNamesList = getStocks(tickerListDto)
                .getStocks()
                .stream()
                .map(StockDto::getName)
                .toList();
        return new CompanyListDto(companyNamesList);
    }

    public CandleListDto getCandleList(CandlesRequestDto candlesRequestDto) {
        candleRequestValidator.validateTimePeriodByInterval(candlesRequestDto);
        ContentDto allCandles = issMoexClient
                .getCandleList(candlesRequestDto, 0)
                .block()
                .getContentMap()
                .get(MOEX_CANDLES_TABLE_NAME);
        contentValidator.validateRequestWithTicker(allCandles, candlesRequestDto.getInstrumentId());

        allCandles.getData()
                .addAll(getAdditionalCandles(candlesRequestDto, MAX_CANDLES_PER_REQUEST).getData());
        List<CandleDto> candleDtoList = contentMapper
                .contentToPojoListUsingColumns(allCandles, CandleDto.class);
        return new CandleListDto(candleAggregator.aggregateCandles(candleDtoList,
                candlesRequestDto.getInterval()));
    }

    private ContentDto getAdditionalCandles(CandlesRequestDto candlesRequestDto, int startParameter) {
        ContentDto additionalCandles = new ContentDto();
        ContentDto candles;
        while (!((candles = issMoexClient
                .getCandleList(candlesRequestDto, startParameter)
                .block()
                .getContentMap()
                .get(MOEX_CANDLES_TABLE_NAME))
                .getData()
                .isEmpty())) {
            additionalCandles.getData().addAll(candles.getData());
            startParameter += MAX_CANDLES_PER_REQUEST;
        }
        return additionalCandles;
    }
}
