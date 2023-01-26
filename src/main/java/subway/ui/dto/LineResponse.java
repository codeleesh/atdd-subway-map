package subway.ui.dto;

import subway.domain.Line;
import subway.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

public class LineResponse {
    private final long id;
    private final String name;
    private final String color;
    private final List<StationResponse> stationResponses;

    public LineResponse(final long id, final String name, final String color, final List<StationResponse> stationResponses) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stationResponses = stationResponses;
    }

    public static LineResponse createResponse(final Line saveLine) {
        return new LineResponse(saveLine.getId(), saveLine.getName(), saveLine.getColor(), convertToStationResponse(saveLine.getStations()));
    }

    private static List<StationResponse> convertToStationResponse(final List<Station> stations) {
        return stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<StationResponse> getStationResponses() {
        return stationResponses;
    }
}