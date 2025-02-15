package life.khabanh.usersservices.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("khabanh.life API Document").version("1.0"))
                .addServersItem(new io.swagger.v3.oas.models.servers.Server().url("http://localhost:10000"))
                .components(new io.swagger.v3.oas.models.Components());
    }
}

