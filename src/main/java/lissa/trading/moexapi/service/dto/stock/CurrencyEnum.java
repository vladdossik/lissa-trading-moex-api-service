package lissa.trading.moexapi.service.dto.stock;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Getter
@Slf4j
public enum CurrencyEnum {
    RUB("SUR");

    private final String legacyCode;
    private final static Map<String, CurrencyEnum> legacyCodeToEnum = new HashMap<>();

    static {
        for (CurrencyEnum currency : values()) {
            legacyCodeToEnum.put(currency.getLegacyCode(), currency);
        }
    }

    public static String mapLegacyToModern(String legacyCode) {
        CurrencyEnum currency = legacyCodeToEnum.get(legacyCode);
        if (currency == null) {
            log.error("Cannot map legacy code to modern; No modern code for {}", legacyCode);
            return legacyCode;
        }
        return currency.name();
    }
}
