package ru.blaskowitz.lissatradingmoexapiservice.dto.candle;

import ru.blaskowitz.lissatradingmoexapiservice.exception.NotFoundException;

import java.util.List;

public enum CandleIntervalEnum {
    CANDLE_INTERVAL_5_MIN(5),
    CANDLE_INTERVAL_HOUR(60),
    CANDLE_INTERVAL_DAY(1440),
    CANDLE_INTERVAL_WEEK(10080);

    private final int value;
    private static final List<Integer> MOEX_SUPPORTED_INTERVALS = List.of(1, 10, 60);

    CandleIntervalEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

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
}
