package lissa.trading.moexapi.service.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                              .title("API для работы с Московской биржей")
                              .description(
                                      "Это API предоставляет функционал по получению информации" +
                                              " о бумагах и компаниях представленных на Московской бирже")
                              .version("1.0.0")
                              .contact(new Contact()
                                               .name("Сергей")
                                               .url("https://t.me/Iswkk"))
                              .license(new License()
                                               .name("Apache 2.0")
                                               .url("https://springdoc.org")))
                .components(new Components()
                                    .addSecuritySchemes("token-key", new SecurityScheme()
                                            .type(SecurityScheme.Type.APIKEY)
                                            .in(SecurityScheme.In.HEADER)
                                            .name("Authorization")));
    }

    @Bean
    public GroupedOpenApi internalApi() {
        return GroupedOpenApi.builder()
                .group("internal")
                .pathsToMatch("/v1/internal/**")
                .addOpenApiCustomizer(openApi -> openApi
                        .addSecurityItem(new SecurityRequirement().addList("token-key")))
                .build();
    }
}
