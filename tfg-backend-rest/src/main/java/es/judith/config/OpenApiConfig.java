package es.judith.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
@SecurityScheme(
        name = "Authorization",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "Opaque token",
        scheme = "bearer"
)
public class OpenApiConfig {

  @Bean
  OpenAPI openAPI() {
    return new OpenAPI().info(apiInfo());
  }

  private Info apiInfo() {
    return new Info().title("REST API of Cuéntame").description("REST API that fetches data from Cuéntame TV series")
        .termsOfService("http://en.wikipedia.org/wiki/Terms_of_service")
        .license(new License().name("Apache License Version 2.0")
            .url("http://www.apache.org/licenses/LICENSE-2.0.html"))
        .version("2.0");
  }
}