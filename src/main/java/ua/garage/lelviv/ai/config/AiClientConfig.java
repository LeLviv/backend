package ua.garage.lelviv.ai.config;

import com.google.genai.Client;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ua.garage.lelviv.ai.client.AiClient;
import ua.garage.lelviv.ai.client.GeminiClient;

@Configuration
@EnableConfigurationProperties(AiBaseProperties.class)
public class AiClientConfig {

    @Bean
    public AiClient geminiClient(Client client) {
        return new GeminiClient(client);
    }

    @Bean
    public Client client(AiBaseProperties properties) {
        return Client.builder().apiKey(properties.geminiApiKey()).build();
    }
}
