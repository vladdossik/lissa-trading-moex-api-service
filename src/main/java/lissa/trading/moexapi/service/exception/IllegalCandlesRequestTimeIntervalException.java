package lissa.trading.moexapi.service.exception;

public class IllegalCandlesRequestTimeIntervalException extends RuntimeException {
    public IllegalCandlesRequestTimeIntervalException(String message) {
        super(message);
    }
}
