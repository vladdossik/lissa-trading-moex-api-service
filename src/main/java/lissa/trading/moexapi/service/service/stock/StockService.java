package lissa.trading.moexapi.service.service.stock;

import lissa.trading.moexapi.service.dto.candle.CandleListDto;
import lissa.trading.moexapi.service.dto.candle.CandlesRequestDto;
import lissa.trading.moexapi.service.dto.company.CompanyListDto;
import lissa.trading.moexapi.service.dto.ticker.TickerListDto;
import lissa.trading.moexapi.service.dto.stock.StockDto;
import lissa.trading.moexapi.service.dto.stock.StockDtoList;
import lissa.trading.moexapi.service.dto.stock.StockPriceDtoList;

public interface StockService {

    StockDto getStock(String ticker);

    StockDtoList getStocks(TickerListDto tickerListDto);

    StockPriceDtoList getStockPrices(TickerListDto tickerListDto);

    CompanyListDto getCompanyNames(TickerListDto tickerListDto);

    CandleListDto getCandleList(CandlesRequestDto candlesRequestDto);
}
