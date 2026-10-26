package ua.garage.lelviv.complaints;

import jakarta.validation.Valid;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/complaints")
@RequiredArgsConstructor
public class ComplaintsController {

    private final ComplaintsService service;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ComplaintResponse> complain(
            @Valid @ModelAttribute ComplaintRequest request, Principal principal) {
        String username = principal.getName();
        return ResponseEntity.ok(service.processComplaint(request, username));
    }
}
