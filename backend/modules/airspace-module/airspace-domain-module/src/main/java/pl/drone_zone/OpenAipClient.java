package pl.drone_zone;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.function.Consumer;

@Configuration
public class OpenAipClient {

    @Value("${open-aip.url}")
    private String url;

    private final String token = System.getenv("OPENAIP_TOKEN");

    @Bean("OPEN_AIP")
    public WebClient openAipClient() {
        Consumer<HttpHeaders> headers = httpHeaders -> {
            httpHeaders.add("x-openaip-api-key", token);
        };

        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs()
                        .maxInMemorySize(16 * 1024 * 1024))
                .build();

        return WebClient.builder()
                .baseUrl(url)
                .defaultHeaders(headers)
                .exchangeStrategies(strategies)
                .build();
    }
}
