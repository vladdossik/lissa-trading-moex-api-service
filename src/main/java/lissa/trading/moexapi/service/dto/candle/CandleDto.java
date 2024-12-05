package lissa.trading.moexapi.service.dto.candle;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandleDto {
    private Long volume;
    private Double open;
    private Double close;
    private Double high;
    private Double low;
    private Boolean isComplete;

    @JsonProperty("time")
    private OffsetDateTime begin;
}
