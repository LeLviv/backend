package ua.garage.lelviv.air_raid;

import java.util.List;

public record AirRaidAPIResponse(
        String regionId,
        String regionType,
        String regionName,
        String regionEngName,
        String lastUpdate,
        List<AirRaidAlert> activeAlerts) {}
