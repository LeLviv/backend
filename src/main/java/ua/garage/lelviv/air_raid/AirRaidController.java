package ua.garage.lelviv.air_raid;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/air-raid")
@RequiredArgsConstructor
public class AirRaidController {

    private final AirRaidService service;
    private final Cache<String, AirRaidStatus> airRaidCache;

    @PostMapping("/set")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void setAirRaidStatus(@RequestParam(name = "status") AirRaidStatus status) {
        airRaidCache.put("STATUS", status);
    }

    @GetMapping("/status")
    public ResponseEntity<String> get_air_raid_status() {
        AirRaidStatus s = airRaidCache.getIfPresent("STATUS");
        System.out.println("Querying air raid status " + s);
        int f = service.dummyMethod();
        System.out.println(f);
        if (s == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(s.toString());
    }
}
