package ua.garage.lelviv.air_raid;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.garage.lelviv.air_raid.entities.AlertStatus;
import ua.garage.lelviv.air_raid.entities.AlertType;

@RestController
@RequestMapping("/api/v1/air-raid")
@RequiredArgsConstructor
public class AirRaidController {
    private final AirRaidService service;

    @PostMapping("/set")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void setAirRaidStatus(
            @RequestParam(name = "type") AlertType type, @RequestParam(name = "status") AlertStatus status) {
        service.setStatus(type, status);
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

    @GetMapping
    public ResponseEntity<String> getAirRaidStatus() {
        AlertStatus s = service.getStatus(AlertType.AIR);
        if (s == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(s.toString());
    }
}
