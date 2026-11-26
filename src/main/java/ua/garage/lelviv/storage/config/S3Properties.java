package ua.garage.lelviv.storage.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app.storage.s3")
public record S3Properties(
        @NotBlank(message = "S3 bucket must not be blank")
        String bucket,

        @NotBlank(message = "S3 region must not be blank")
        @DefaultValue("us-east-1")
        String region,

        String publicUrlPrefix,

        @Valid
        Local local) {

    @Validated
    public record Local(
            @NotBlank(message = "accessKey is required for local S3")
            @Size(min = 3, message = "accessKey must be at least 3 characters")
            String accessKey,

            @NotBlank(message = "secretKey is required for local S3")
            @Size(min = 8, message = "secretKey must be at least 8 characters")
            String secretKey,

            @NotBlank(message = "endpoint is required for local S3")
            @URL(message = "endpoint must be a valid URL")
            String endpoint) {}
}
