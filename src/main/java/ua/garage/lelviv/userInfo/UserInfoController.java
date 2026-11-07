package ua.garage.lelviv.userInfo;

import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/userInfo")
@RequiredArgsConstructor
public class UserInfoController {

    private final UserInfoService service;

    @GetMapping("/username")
    public ResponseEntity<String> getUsername(Principal principal) {
        String email = principal.getName();
        return ResponseEntity.ok(service.getUsername(email));
    }
}
