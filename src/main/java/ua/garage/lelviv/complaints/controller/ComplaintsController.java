package ua.garage.lelviv.complaints.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ua.garage.lelviv.complaints.dto.ComplaintDto;
import ua.garage.lelviv.complaints.dto.ComplaintRequest;
import ua.garage.lelviv.complaints.dto.ComplaintResponse;
import ua.garage.lelviv.complaints.service.ComplaintsService;

@RestController
@RequestMapping("/api/v1/complaints")
@RequiredArgsConstructor
public class ComplaintsController {
    private final ComplaintsService service;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ComplaintResponse> complain(
            @Valid @ModelAttribute ComplaintRequest request, Principal principal) {
        String username = principal.getName();
        return ResponseEntity.ok(service.processComplaint(request, username));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<ComplaintDto>> getComplaints(
            @Min(0) @RequestParam(defaultValue = "0") int page,
            @Min(1) @Max(100) @RequestParam(defaultValue = "20") int size) {

        Page<ComplaintDto> complainsPage = service.getComplaints(page, size);

        return ResponseEntity.ok(complainsPage);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ComplaintDto> getComplaint(@PathVariable Long id) {
        ComplaintDto complaint = service.getComplaint(id);

        return ResponseEntity.ok(complaint);
    }
}
