package ua.garage.lelviv.ai.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.garage.lelviv.ai.client.AiClient;
import ua.garage.lelviv.ai.entity.ChatRequest;
import ua.garage.lelviv.ai.entity.ChatResponse;

@Service
@RequiredArgsConstructor
public class AiService {

    private final AiClient aiClient;

    public ChatResponse ask(ChatRequest request) {
        return aiClient.ask(request.query());
    }
}
