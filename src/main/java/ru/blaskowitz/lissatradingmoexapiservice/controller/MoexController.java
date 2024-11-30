package ru.blaskowitz.lissatradingmoexapiservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.blaskowitz.lissatradingmoexapiservice.dto.Company.CompanyListDto;
import ru.blaskowitz.lissatradingmoexapiservice.dto.candle.CandleListDto;
import ru.blaskowitz.lissatradingmoexapiservice.dto.candle.CandlesRequestDto;
import ru.blaskowitz.lissatradingmoexapiservice.dto.secid.TickerListDto;
import ru.blaskowitz.lissatradingmoexapiservice.dto.stock.StockDto;
import ru.blaskowitz.lissatradingmoexapiservice.dto.stock.StockDtoList;
import ru.blaskowitz.lissatradingmoexapiservice.dto.stock.StockPriceDtoList;
import ru.blaskowitz.lissatradingmoexapiservice.service.candle.CandleService;
import ru.blaskowitz.lissatradingmoexapiservice.service.stock.StockService;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/v1/moex")
public class MoexController {

    private final StockService stockService;
    private final CandleService candleService;

    @GetMapping("/{ticker}")
    public StockDto getStock(@PathVariable String ticker) {
        log.info("Requesting getStock endpoint with ticker: {} ", ticker);
        return stockService.getStock(ticker);
    }

    @PostMapping("/stocks")
    public StockDtoList getStocks(@RequestBody TickerListDto tickerListDto) {
        log.info("Requesting getStocks endpoint with params: {} ", tickerListDto);
        return stockService.getStocks(tickerListDto);
    }

    @PostMapping("/stocks/prices")
    public StockPriceDtoList getStocksPrices(@RequestBody TickerListDto tickerListDto) {
        log.info("Requesting getStockPrices endpoint with params: {} ", tickerListDto);
        return stockService.getStockPrices(tickerListDto);
    }

    @PostMapping("/companies")
    public CompanyListDto getCompanies(@RequestBody TickerListDto tickerListDto) {
        log.info("Requesting getCompanies endpoint with params: {} ", tickerListDto);
        return stockService.getCompanyNames(tickerListDto);
    }

    @PostMapping("/candles")
    public CandleListDto getCandles(@RequestBody CandlesRequestDto candlesRequestDto) {
        log.info("Requesting getCandles endpoint with param: {} ", candlesRequestDto);
        return candleService.getCandleList(candlesRequestDto);
    }
}
