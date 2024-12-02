package lissa.trading.moexapi.service.dto.candle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandleListDto {
    private List<CandleDto> candles;
}
