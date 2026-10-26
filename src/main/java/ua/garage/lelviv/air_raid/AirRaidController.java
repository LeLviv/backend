package ua.garage.lelviv.air_raid;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/air-raid")
@RequiredArgsConstructor
public class AirRaidController {

    @PostMapping("/activate")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void activate() {

    }
}
