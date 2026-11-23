package ua.garage.lelviv.ai.client;

import com.google.genai.Client;
import com.google.genai.gaos.models.interactions.*;
import com.google.genai.gaos.models.operations.CreateInteractionRequestBody;
import lombok.RequiredArgsConstructor;
import ua.garage.lelviv.ai.entity.ChatResponse;

@RequiredArgsConstructor
public class GeminiClient implements AiClient {

    private final Client client;

    public ChatResponse ask(String query) {
        CreateModelInteraction params = CreateModelInteraction.builder()
                .model(Model.GEMINI35_FLASH_LITE)
                .input(InteractionsInput.of(query))
                .build();

        Interaction interaction = client.interactions
                .create(CreateInteractionRequestBody.of(params))
                .interaction()
                .orElseThrow();
        return new ChatResponse(interaction.outputText().orElse(""));
    }
}
