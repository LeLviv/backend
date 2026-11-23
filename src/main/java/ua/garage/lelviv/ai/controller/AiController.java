package ua.garage.lelviv.ai.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.garage.lelviv.ai.entity.ChatRequest;
import ua.garage.lelviv.ai.entity.ChatResponse;
import ua.garage.lelviv.ai.entity.DayPlanRequest;
import ua.garage.lelviv.ai.entity.DayPlanResponse;
import ua.garage.lelviv.ai.service.AiService;
import ua.garage.lelviv.ai.service.DayPlanService;

@RestController
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;
    private final DayPlanService dayPlanService;

    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> ask(@Valid @RequestBody ChatRequest request) {
        return ResponseEntity.ok(aiService.ask(request));
    }

    @PostMapping("/plan")
    public ResponseEntity<DayPlanResponse> plan(@Valid @RequestBody DayPlanRequest request) {
        return ResponseEntity.ok(dayPlanService.generatePlan(request));
    }
}
