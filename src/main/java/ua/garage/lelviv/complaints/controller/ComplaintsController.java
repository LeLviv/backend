package ua.garage.lelviv.complaints.controller;

import jakarta.validation.Valid;
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
import ua.garage.lelviv.complaints.entities.Complaint;
import ua.garage.lelviv.complaints.service.ComplaintsService;

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

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<ComplaintDto>> getComplaints(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {

        Page<ComplaintDto> complainsPage = service.getComplaints(page, size).map(this::toDto);

        return ResponseEntity.ok(complainsPage);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ComplaintDto> getComplaint(@PathVariable Long id) {
        Complaint complaint = service.getComplaint(id);

        return ResponseEntity.ok(toDto(complaint));
    }

    private ComplaintDto toDto(Complaint entity) {
        return ComplaintDto.builder()
                .id(entity.getId())
                .author(entity.getAuthor())
                .problem(entity.getProblem())
                .photoPath(entity.getPhotoPath())
                .latitude(entity.getLatitude())
                .longitude(entity.getLongitude())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
