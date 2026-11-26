package ua.garage.lelviv.survey.controller;

import jakarta.validation.Valid;
import java.time.Instant;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.garage.lelviv.authorization.entities.User;
import ua.garage.lelviv.survey.dto.request.MakeSurveyRequest;
import ua.garage.lelviv.survey.dto.request.VoteRequest;
import ua.garage.lelviv.survey.dto.response.IdResponse;
import ua.garage.lelviv.survey.dto.response.SurveyResponseDto;
import ua.garage.lelviv.survey.service.SurveyService;

@RestController
@RequestMapping("/api/v1/surveys")
@RequiredArgsConstructor
@Validated
public class SurveyController {

    private final SurveyService service;

    @PostMapping("/{surveyId}/vote")
    public ResponseEntity<Void> vote(
            @PathVariable Long surveyId,
            @Valid @RequestBody VoteRequest request,
            @AuthenticationPrincipal User currentUser) {
        service.vote(surveyId, request, currentUser.getId());

        return ResponseEntity.ok().build();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<IdResponse> makeSurvey(@Valid @RequestBody MakeSurveyRequest request) {
        Long newId = service.makeSurvey(request);

        return ResponseEntity.status(HttpStatus.CREATED).body((new IdResponse(newId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SurveyResponseDto> getSurvey(@PathVariable Long id) {
        return ResponseEntity.ok(service.getSurvey(id));
    }

    @GetMapping
    public ResponseEntity<Page<SurveyResponseDto>> getSurveys(
            @Min(0) @RequestParam(defaultValue = "0") int page, @Min(1) @Max(100) @RequestParam(defaultValue = "20") int size) {
        Page<SurveyResponseDto> surveysPage = service.getSurveys(page, size);

        return ResponseEntity.ok(surveysPage);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelSurvey(@PathVariable Long id) {
        service.cancelSurvey(id);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/{id}/expiration")
    public ResponseEntity<Void> changeSurveyExpiration(
            @PathVariable Long id,
            @RequestParam("newTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant newTime) {
        service.changeSurveyExpiration(id, newTime);

        return ResponseEntity.ok().build();
    }
}
