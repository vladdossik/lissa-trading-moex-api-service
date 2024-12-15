package lissa.trading.moexapi.service.dto.candle;

import lissa.trading.moexapi.service.exception.IllegalCandlesRequestTimeIntervalException;
import lissa.trading.moexapi.service.exception.IllegalCandlesRequestZoneOffsetException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.ZoneOffset;

@Component
@Slf4j
public class CandleRequestValidator {
    public void validateTimePeriodByInterval(CandlesRequestDto candlesRequestDto) {
        long timeInterval = Duration
                .between(candlesRequestDto.getFrom(), candlesRequestDto.getTill())
                .toMinutes();
        if (timeInterval <= 0) {
            throw new IllegalCandlesRequestTimeIntervalException(
                    "Invalid time interval in candlesRequest: 'till must be after from'");
        }
        CandleIntervalEnum candleInterval = candlesRequestDto.getInterval();
        if (!candleInterval.isIntervalWithinLimit(timeInterval)) {
            throw new IllegalCandlesRequestTimeIntervalException(String.format(
                    "Invalid time interval interval in candle request:" +
                            " 'Time range exceeds maximum allowed interval" +
                            " for %s. Max allowed: %d minutes'",
                    candleInterval.name(), candleInterval.getMaxAllowedTimeInterval()));
        }
    }
}
