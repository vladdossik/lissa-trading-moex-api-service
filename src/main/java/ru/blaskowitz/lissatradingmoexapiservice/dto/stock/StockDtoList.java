package ru.blaskowitz.lissatradingmoexapiservice.dto.stock;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockDtoList {
    private List<StockDto> stocks;
}
