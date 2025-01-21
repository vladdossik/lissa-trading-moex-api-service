package lissa.trading.moexapi.service.dto.ticker;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TickerListDto {
    private List<String> tickers;
}
