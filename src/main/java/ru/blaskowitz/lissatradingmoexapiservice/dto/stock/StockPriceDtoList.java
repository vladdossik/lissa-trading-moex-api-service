package ru.blaskowitz.lissatradingmoexapiservice.dto.stock;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockPriceDtoList {
    private List<StockPriceDto> stockPrices;
}
