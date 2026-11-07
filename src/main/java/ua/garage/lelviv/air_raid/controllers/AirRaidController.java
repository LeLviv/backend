package ua.garage.lelviv.air_raid.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.garage.lelviv.air_raid.AirRaidService;
import ua.garage.lelviv.air_raid.entities.AlertStatus;
import ua.garage.lelviv.air_raid.entities.AlertType;

@RestController
@RequestMapping("/api/v1/air-raid")
@RequiredArgsConstructor
public class AirRaidController {
    private final AirRaidService service;

    @GetMapping
    public ResponseEntity<String> getAirRaidStatus() {
        AlertStatus s = service.getStatus(AlertType.AIR);
        if (s == null) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok(s.toString());
    }
}
