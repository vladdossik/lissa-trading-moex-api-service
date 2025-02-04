package lissa.trading.moexapi.service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
@RequestMapping("/v1/internal")
public class MoexController {

    private final StockService stockService;

    @Operation(summary = "Получить информацию об акции", description = "Возвращает информацию об акции по тикеру.")
    @ApiResponse(description = "Информация об акции успешно получена",
            content = @Content(schema = @Schema(implementation = StockDto.class)))
    @GetMapping("/{ticker}")
    public StockDto getStock(@PathVariable String ticker) {
        return stockService.getStock(ticker);
    }

    @Operation(summary = "Получить информацию о нескольких акциях", description = "Возвращает информацию о нескольких акциях по переданным тикерам.")
    @ApiResponse(description = "Информация о нескольких акциях успешно получена",
            content = @Content(schema = @Schema(implementation = StockDtoList.class)))
    @PostMapping("/stocks")
    public StockDtoList getStocks(@RequestBody TickerListDto tickerListDto) {
        return stockService.getStocks(tickerListDto);
    }

    @Operation(summary = "Получить цены акций", description = "Возвращает текущие цены акций по переданным figies.")
    @ApiResponse(description = "Цены акций успешно получены",
            content = @Content(schema = @Schema(implementation = StockPriceDtoList.class)))
    @PostMapping("/stocks/prices")
    public StockPriceDtoList getStocksPrices(@RequestBody TickerListDto tickerListDto) {
        return stockService.getStockPrices(tickerListDto);
    }

    @Operation(summary = "Получить названия компаний", description = "Возвращает названия компаний по тикерам")
    @ApiResponse(description = "Названия компаний успешно получены",
            content = @Content(schema = @Schema(implementation = CompanyListDto.class)))
    @PostMapping("/companies")
    public CompanyListDto getCompanies(@RequestBody TickerListDto tickerListDto) {
        return stockService.getCompanyNames(tickerListDto);
    }

    @Operation(summary = "Получить исторические данные об инструменте", description = "Возвращает свечи за выбранный " +
            "период времени" )
    @ApiResponse(description = "Цены акций успешно получены",
            content = @Content(schema = @Schema(implementation = CandleListDto.class)))
    @PostMapping("/candles")
    public CandleListDto getCandles(@RequestBody CandlesRequestDto candlesRequestDto) {
        return stockService.getCandleList(candlesRequestDto);
    }
}
