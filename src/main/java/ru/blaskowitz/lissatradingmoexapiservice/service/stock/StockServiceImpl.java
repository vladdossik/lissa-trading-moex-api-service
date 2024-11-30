package ru.blaskowitz.lissatradingmoexapiservice.service.stock;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.blaskowitz.lissatradingmoexapiservice.client.IssMoexClient;
import ru.blaskowitz.lissatradingmoexapiservice.dto.Company.CompanyListDto;
import ru.blaskowitz.lissatradingmoexapiservice.dto.moex.Content;
import ru.blaskowitz.lissatradingmoexapiservice.dto.moex.ContentValidator;
import ru.blaskowitz.lissatradingmoexapiservice.dto.secid.TickerListDto;
import ru.blaskowitz.lissatradingmoexapiservice.dto.stock.StockDto;
import ru.blaskowitz.lissatradingmoexapiservice.dto.stock.StockDtoList;
import ru.blaskowitz.lissatradingmoexapiservice.dto.stock.StockPriceDto;
import ru.blaskowitz.lissatradingmoexapiservice.dto.stock.StockPriceDtoList;
import ru.blaskowitz.lissatradingmoexapiservice.service.ContentMapper;

import java.util.ArrayList;
import java.util.List;

import static ru.blaskowitz.lissatradingmoexapiservice.service.ServiceConstants.MOEX_STOCK_INFO_TABLE_NAME;
import static ru.blaskowitz.lissatradingmoexapiservice.service.ServiceConstants.MOEX_STOCK_PRICES_TABLE_NAME;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockServiceImpl implements StockService {

    private final IssMoexClient issMoexClient;
    private final ContentMapper contentMapper;
    private final ContentValidator contentValidator;

    @Override
    public StockDto getStock(String ticker) {
        Content content = issMoexClient
                .getStockByTicker(ticker)
                .block()
                .getContentMap()
                .get(MOEX_STOCK_INFO_TABLE_NAME);

        contentValidator.validateWithSecId(content, ticker);
        StockDto stockDto = contentMapper.ContentToPojo(content, StockDto.class);
        return stockDto;
    }

    @Override
    public StockDtoList getStocks(TickerListDto tickerListDto) {
        List<StockDto> stockDtoList = new ArrayList<>();
        tickerListDto.getTickerList().forEach(ticker -> {
           StockDto stockDto = getStock(ticker);
           stockDtoList.add(stockDto);
        });
        return new StockDtoList(stockDtoList);
    }

    @Override
    public StockPriceDtoList getStockPrices(TickerListDto tickerListDto) {
        List<StockPriceDto> stockPriceDtoList = new ArrayList<>();
        tickerListDto.getTickerList().forEach(ticker -> {
            Content content = issMoexClient
                    .getStockPriceByTicker(ticker)
                    .block()
                    .getContentMap()
                    .get(MOEX_STOCK_PRICES_TABLE_NAME);
            contentValidator.validateWithSecId(content, ticker);
            stockPriceDtoList.add(contentMapper.ContentToPojoUsingColumns(content, StockPriceDto.class));
        });
        return new StockPriceDtoList(stockPriceDtoList);
    }

    @Override
    public CompanyListDto getCompanyNames(TickerListDto tickerListDto) {
        List<String> companyNamesList  = new ArrayList<>();

        getStocks(tickerListDto)
                .getStocks()
                .stream()
                .forEach(stock -> companyNamesList
                        .add(stock.getName()));

        return new CompanyListDto(companyNamesList);
    }
}
