package lissa.trading.moexapi.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "lissa.trading")
public class LissaTradingMoexApiServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LissaTradingMoexApiServiceApplication.class, args);
    }

}
