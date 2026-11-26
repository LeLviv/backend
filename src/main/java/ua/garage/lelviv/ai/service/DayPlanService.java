package ua.garage.lelviv.ai.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.garage.lelviv.ai.entity.ChatRequest;
import ua.garage.lelviv.ai.entity.ChatResponse;
import ua.garage.lelviv.ai.entity.DayPlanRequest;
import ua.garage.lelviv.ai.entity.DayPlanResponse;
import ua.garage.lelviv.weather.WeatherService;
import ua.garage.lelviv.weather.entity.WeatherHourlyDto;

@Service
@RequiredArgsConstructor
public class DayPlanService {

    private final WeatherService weatherService;
    private final AiService aiService;

    public DayPlanResponse generatePlan(DayPlanRequest dayPlanRequest) {
        WeatherHourlyDto forecast = weatherService.getWeatherHourly(dayPlanRequest.region());
        ChatRequest prompt = new ChatRequest(buildPrompt(dayPlanRequest, forecast));
        ChatResponse chatResponse = aiService.ask(prompt);
        return new DayPlanResponse(chatResponse.reply());
    }

    private String buildPrompt(DayPlanRequest dayPlanRequest, WeatherHourlyDto forecast) {
        StringBuilder sb = new StringBuilder();

        sb.append("Create a realistic daily plan and a logical movement route in Lviv.\n");
        sb.append("District/Area: ").append(dayPlanRequest.region()).append("\n");
        sb.append("Request date/time: ").append(dayPlanRequest.time()).append("\n\n");

        sb.append("Hourly weather forecast (WMO weather codes):\n");
        for (WeatherHourlyDto.Report report : forecast.reports()) {
            sb.append("{\n");
            sb.append("- Time: ").append(report.time()).append("\n");
            sb.append("  | Temp: ").append(report.tempCelsius()).append("\n");
            sb.append("  | Precipitation prob: ")
                    .append(report.precipitationProbPercent())
                    .append("%\n");
            sb.append("  | WMO code: ").append(report.weatherCode()).append("\n");
            sb.append("}\n");
        }

        sb.append("\nPower outage schedule:\n");
        sb.append("- No outages scheduled or schedule not yet published.\n");

        sb.append("\nAvailable events:\n");
        sb.append(
                "- No events in the list; suggest walks, cafes, parks, or indoor venues based on weather and other factors.\n");

        sb.append("""

                Requirements:
                1. Start the response directly with the plan heading—do NOT include any introductory paragraphs, greetings, or closing summaries.
                2. Structure the plan by time slots (morning, afternoon, evening), starting at the request date/time and ending at the end of that same calendar day (do not include activities on the next day). Skip time slots that have already passed relative to the request date/time.
                3. Use the weather forecast only as background context to choose suitable activities and route pacing; do not dwell on weather descriptions in every slot, do not recite temperatures/probabilities unless practically relevant, and never mention WMO codes or raw forecast numbers.
                4. Describe a logical sequence of movements (route) between stops without inventing non-existent facts, closed venues, or fake events.
                5. If there are long gaps without events, suggest suitable activities matching the context.
                6. Keep the response concise, actionable, and written entirely in Ukrainian, keeping location names natural in Ukrainian.
                """);

        return sb.toString();
    }
}
