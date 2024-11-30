package ru.blaskowitz.lissatradingmoexapiservice.dto.stock;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockDto {
    @JsonProperty("ticker")
    private String secid;

    private String figi;

    private String name;

    private String type;

    @JsonProperty("Currency")
    private String faceunit;

    private String source;
}
