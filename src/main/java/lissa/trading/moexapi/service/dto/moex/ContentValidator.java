package lissa.trading.moexapi.service.dto.moex;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import lissa.trading.moexapi.service.exception.NotFoundException;
import org.springframework.util.CollectionUtils;

@Component
@Slf4j
public class ContentValidator {

    public void validateRequestWithTicker(ContentDto contentDto, String ticker) {
        if (CollectionUtils.isEmpty(contentDto.getData())) {
            log.error("Moex returned null or empty response for ticker: {}", ticker);
            throw new NotFoundException("ticker: " + ticker + " not found");
        }
    }
}
