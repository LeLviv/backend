package ua.garage.lelviv.weather.config;

import java.net.http.HttpClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import ua.garage.lelviv.weather.exception.WeatherProviderException;

@Configuration
@EnableConfigurationProperties(WeatherProvidersProperties.class)
public class WeatherClientConfig {
    @Bean
    public RestClient openMeteoRestClient(WeatherProvidersProperties properties) {
        var config = properties.provider().get("open-meteo");
        return createRestClient(config);
    }

    @Bean
    public RestClient openWeatherRestClient(WeatherProvidersProperties properties) {
        var config = properties.provider().get("open-weather");
        return createRestClient(config);
    }

    private RestClient createRestClient(WeatherProvidersProperties.ProviderConfig config) {
        var httpClient =
                HttpClient.newBuilder().connectTimeout(config.connectTimeout()).build();

        var requestFactory = new JdkClientHttpRequestFactory(httpClient);
        requestFactory.setReadTimeout(config.readTimeout());

        return RestClient.builder()
                .baseUrl(config.baseUrl())
                .requestFactory(requestFactory)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultStatusHandler(HttpStatusCode::isError, (_, response) -> {
                    throw new WeatherProviderException(
                            "Provider [" + config.name() + "] failed with status code " + response.getStatusCode());
                })
                .build();
    }
}
