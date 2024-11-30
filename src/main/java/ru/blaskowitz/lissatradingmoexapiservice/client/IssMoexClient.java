package ru.blaskowitz.lissatradingmoexapiservice.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.View;
import reactor.core.publisher.Mono;
import ru.blaskowitz.lissatradingmoexapiservice.dto.candle.CandlesRequestDto;
import ru.blaskowitz.lissatradingmoexapiservice.dto.moex.MoexUniversalDto;

@Component
@RequiredArgsConstructor
@Slf4j
public class IssMoexClient {

    private final WebClient issMoexWebClient;
    private final View error;


    public Mono<MoexUniversalDto> getStockByTicker(String ticker) {
        return issMoexWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(String.format("/securities/%s.json", ticker))
                        .queryParam("iss.only", "description")
                        .queryParam("iss.meta", "off")
                        .queryParam("description.columns", "name,value")
                        .build())
                .retrieve()
                .bodyToMono(MoexUniversalDto.class)
                .doOnError(error -> log.error(error.getMessage(), error));
    }


    public Mono<MoexUniversalDto> getStockPriceByTicker(String ticker) {
        return issMoexWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(String.format("/engines/stock/markets/shares/boards/TQBR/securities/%s.json", ticker))
                        .queryParam("iss.meta", "off")
                        .queryParam("iss.only", "marketdata")
                        .queryParam("marketdata.columns", "LAST")
                        .build())
                .retrieve()
                .bodyToMono(MoexUniversalDto.class)
                .doOnError(error -> log.error(error.getMessage(), error));
    }

    public Mono<MoexUniversalDto> getCandleList(CandlesRequestDto candlesRequestDto, int start) {
        return issMoexWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(String.format("/engines/stock/markets/shares/boards/TQBR/securities/%s/candles.json",
                                candlesRequestDto.getInstrumentId()))
                        .queryParam("from", candlesRequestDto.getFrom().toLocalDate())
                        .queryParam("till", candlesRequestDto.getTill().toLocalDate())
                        .queryParam("interval", candlesRequestDto.getInterval().getMaxSupportedIntervalValue())
                        .queryParam("iss.meta", "off")
                        .queryParam("start", start)
                        .build())
                .retrieve()
                .bodyToMono(MoexUniversalDto.class)
                .doOnError(error -> log.error(error.getMessage(), error));
    }



}
