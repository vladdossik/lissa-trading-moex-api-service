package ru.blaskowitz.lissatradingmoexapiservice.dto.moex;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.blaskowitz.lissatradingmoexapiservice.exception.NotFoundException;


@Component
@Slf4j
public class ContentValidator {

    public void validateWithSecId(Content content, String secid) {
        if (content == null || content.getData().isEmpty()) {
            log.error("Moex returned null or empty response for secid: {}", secid);
            throw new NotFoundException("Secid: " + secid + " not found");
        }
    }

}
