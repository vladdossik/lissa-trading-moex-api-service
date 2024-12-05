package lissa.trading.moexapi.service.dto.stock;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockDto {
    private String figi;
    private String name;
    private String type;
    private String source;

    @JsonProperty("ticker")
    private String secid;

    @JsonProperty("currency")
    private String faceunit;
}
