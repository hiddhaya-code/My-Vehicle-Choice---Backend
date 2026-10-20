package com.myvehiclechoice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MyVehicleChoice REST API")
                        .version("1.0.0")
                        .description("Production-minded vehicle comparison and recommendation platform API for India.")
                        .contact(new Contact()
                                .name("MyVehicleChoice Engineering")
                                .email("support@myvehiclechoice.in"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")));
    }
}
