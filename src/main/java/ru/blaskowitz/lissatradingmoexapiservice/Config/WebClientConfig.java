package ru.blaskowitz.lissatradingmoexapiservice.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.JdkClientHttpConnector;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.http.HttpClient;
import java.time.Duration;


@Configuration
public class WebClientConfig implements WebFluxConfigurer {

    @Value("${integration.rest.iss-moex-url}")
    private String moexUrl;

    @Bean
    public WebClient issMoexWebClient() {

        final HttpClient httpClientWithTimeouts = HttpClient
                .newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();

        return WebClient
                .builder()
                .baseUrl(moexUrl)
                .clientConnector(new JdkClientHttpConnector(httpClientWithTimeouts))
                .build();
    }
}
