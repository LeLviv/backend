package ua.garage.lelviv.air_raid.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import ua.garage.lelviv.air_raid.AirRaidService;
import ua.garage.lelviv.air_raid.config.AirRaidProperties;
import ua.garage.lelviv.air_raid.entities.AlertStatus;
import ua.garage.lelviv.air_raid.entities.AlertType;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/admin/air-raid/")
@PreAuthorize("hasRole('ADMIN')")
@Validated
public class AirRaidAdminController {
    private final AirRaidService service;
    private final AirRaidProperties properties;

    @PostMapping("/set")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void setAirRaidStatus(
            @RequestParam(name = "type") AlertType type, @RequestParam(name = "status") AlertStatus status) {
        service.setStatus(type, status);
        if (properties.sendNotificationOnUpdate()) {
            service.sendNotification(type);
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
        service.sendNotification(AlertType.AIR);
    }
}
