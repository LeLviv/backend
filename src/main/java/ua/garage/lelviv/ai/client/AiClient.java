package ua.garage.lelviv.ai.client;

import ua.garage.lelviv.ai.entity.ChatResponse;

public interface AiClient {
    ChatResponse ask(String query);
}
