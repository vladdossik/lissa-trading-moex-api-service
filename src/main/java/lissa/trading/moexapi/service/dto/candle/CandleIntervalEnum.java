package lissa.trading.moexapi.service.dto.candle;

import lissa.trading.moexapi.service.exception.NotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public enum CandleIntervalEnum {
    CANDLE_INTERVAL_5_MIN(5, 4320),
    CANDLE_INTERVAL_HOUR(60, 10080),
    CANDLE_INTERVAL_DAY(1440, 241920),
    CANDLE_INTERVAL_WEEK(10080, 483840);

    private final int value;
    private final int maxAllowedTimeInterval;
    private static final List<Integer> MOEX_SUPPORTED_INTERVALS = List.of(1, 10, 60);

    public boolean isMoexSupported() {
        return MOEX_SUPPORTED_INTERVALS.contains(value);
    }

    public int getMaxSupportedIntervalValue() {
        return MOEX_SUPPORTED_INTERVALS
                .stream()
                .filter(supportedValue -> supportedValue <= this.value)
                .max(Integer::compare)
                .orElseThrow(() -> new NotFoundException("Candle interval is not found"));
    }

    public boolean isIntervalWithinLimit(long intervalValue) {
        return this.maxAllowedTimeInterval >= intervalValue;
    }
}
