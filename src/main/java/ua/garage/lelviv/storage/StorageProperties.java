package ua.garage.lelviv.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "app.storage")
public record StorageProperties(
        @DefaultValue("local") String type, @DefaultValue Folders folders) {
    public record Folders(
            @DefaultValue("events") String events,
            @DefaultValue("complaints") String complaints) {}
}
