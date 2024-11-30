package ru.blaskowitz.lissatradingmoexapiservice.dto.stock;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockPriceDto {
    private String figi;

    @JsonProperty("price")
    private double last;
}
