package ru.blaskowitz.lissatradingmoexapiservice.service.stock;

import ru.blaskowitz.lissatradingmoexapiservice.dto.Company.CompanyListDto;
import ru.blaskowitz.lissatradingmoexapiservice.dto.secid.TickerListDto;
import ru.blaskowitz.lissatradingmoexapiservice.dto.stock.StockDto;
import ru.blaskowitz.lissatradingmoexapiservice.dto.stock.StockDtoList;
import ru.blaskowitz.lissatradingmoexapiservice.dto.stock.StockPriceDtoList;

public interface StockService {

    StockDto getStock(String ticker);

    StockDtoList getStocks(TickerListDto tickerListDto);

    StockPriceDtoList getStockPrices(TickerListDto tickerListDto);

    CompanyListDto getCompanyNames(TickerListDto tickerListDto);
}
