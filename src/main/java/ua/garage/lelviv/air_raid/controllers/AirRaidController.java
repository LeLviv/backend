package ua.garage.lelviv.air_raid.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.garage.lelviv.air_raid.entities.AlertStatus;
import ua.garage.lelviv.air_raid.entities.AlertType;
import ua.garage.lelviv.air_raid.services.AirRaidService;

@RestController
@RequestMapping("/api/v1/air-raid")
@RequiredArgsConstructor
public class AirRaidController {
    private final AirRaidService service;

    @GetMapping
    public ResponseEntity<String> getAirRaidStatus() {
        AlertStatus s = service.getStatus(AlertType.AIR);
        if (s == null) {
            s = AlertStatus.UNKNOWN;
        }
        return ResponseEntity.ok(s.toString());
    }
}
