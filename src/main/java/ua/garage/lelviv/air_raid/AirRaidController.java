package ua.garage.lelviv.air_raid;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/air-raid")
@RequiredArgsConstructor
public class AirRaidController {
    private final AirRaidService service;

    @PostMapping("/set")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void setAirRaidStatus(@RequestParam(name = "status") AirRaidStatus status) {
        service.setStatus(status);
    }

    @PostMapping("/update-official")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateOfficialStatus() {
        service.updateAirRaidStatus();
    }

    @GetMapping("/status")
    public ResponseEntity<String> getAirRaidStatus() {
        AirRaidStatus s = service.getStatus();
        System.out.println("Querying air raid status " + s);
        if (s == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(s.toString());
    }
}
