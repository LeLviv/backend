package ua.garage.lelviv.air_raid.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.garage.lelviv.air_raid.config.AirRaidProperties;
import ua.garage.lelviv.air_raid.entities.AlertStatus;
import ua.garage.lelviv.air_raid.entities.AlertType;
import ua.garage.lelviv.air_raid.services.AirRaidService;
import ua.garage.lelviv.air_raid.services.NotificationService;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/admin/air-raid")
@PreAuthorize("hasAuthority('ADMIN')")
@Validated
public class AirRaidAdminController {
    private final AirRaidService service;
    private final AirRaidProperties properties;
    private final NotificationService notificationService;

    @PostMapping("/set")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void setAirRaidStatus(
            @RequestParam(name = "type") AlertType type, @RequestParam(name = "status") AlertStatus status) {
        service.setStatus(type, status);
        if (properties.sendNotificationOnUpdate()) {
            notificationService.sendNotification(type, service.getStatus(type));
        }
    }

    @PostMapping("/update-official")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateOfficialStatus() {
        service.updateAirRaidStatus();
    }

    @PostMapping("/send-notification")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void sendNotification() {
        notificationService.sendNotification(AlertType.AIR, service.getStatus(AlertType.AIR));
    }
}
