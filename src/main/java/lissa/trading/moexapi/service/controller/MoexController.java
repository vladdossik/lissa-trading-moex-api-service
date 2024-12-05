package lissa.trading.moexapi.service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lissa.trading.moexapi.service.dto.company.CompanyListDto;
import lissa.trading.moexapi.service.dto.candle.CandleListDto;
import lissa.trading.moexapi.service.dto.candle.CandlesRequestDto;
import lissa.trading.moexapi.service.dto.ticker.TickerListDto;
import lissa.trading.moexapi.service.dto.stock.StockDto;
import lissa.trading.moexapi.service.dto.stock.StockDtoList;
import lissa.trading.moexapi.service.dto.stock.StockPriceDtoList;
import lissa.trading.moexapi.service.service.stock.StockService;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/v1/moex")
public class MoexController {

    private final StockService stockService;

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
        return stockService.getCandleList(candlesRequestDto);
    }
}
