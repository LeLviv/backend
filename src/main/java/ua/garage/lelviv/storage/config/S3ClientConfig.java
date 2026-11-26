package ua.garage.lelviv.storage.config;

import java.net.URI;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.S3Configuration;

@Slf4j
@Configuration
@EnableConfigurationProperties(S3Properties.class)
@ConditionalOnProperty(name = "app.storage.type", havingValue = "s3")
public class S3ClientConfig {

    private S3ClientBuilder baseBuilder(S3Properties properties) {
        return S3Client.builder()
                .region(Region.of(properties.region()))
                .overrideConfiguration(ClientOverrideConfiguration.builder()
                        .apiCallTimeout(Duration.ofSeconds(30))
                        .apiCallAttemptTimeout(Duration.ofSeconds(10))
                        .build());
    }

    @Bean(destroyMethod = "close")
    @Profile("local")
    @ConditionalOnProperty(prefix = "app.storage.s3.local", name = "endpoint")
    public S3Client localS3Client(S3Properties properties) {
        S3Properties.Local local = properties.local();
        log.info("Initializing local S3Client for endpoint: {}", local.endpoint());

        return baseBuilder(properties)
                .endpointOverride(URI.create(local.endpoint()))
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(true)
                        .build())
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(local.accessKey(), local.secretKey())))
                .build();
    }

    @Bean(destroyMethod = "close")
    @ConditionalOnMissingBean(S3Client.class)
    public S3Client defaultCredentialsProviderS3Client(S3Properties properties) {
        log.info("Initializing S3Client with DefaultCredentialsProvider for region: {}", properties.region());

        return baseBuilder(properties)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }
}
