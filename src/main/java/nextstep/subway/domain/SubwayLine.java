package nextstep.subway.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class SubwayLine {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "subway_line_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "distance")
    private Integer distance;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "color_id")
    private SubwayLineColor color;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "up_station_id")
    private Station upStation;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "down_station_id")
    private Station downStation;

    @OneToMany(mappedBy = "subwayLine", fetch = LAZY)
    private List<StationToSubwayLine> stations = new ArrayList<>();

    protected SubwayLine() {
    }

    public SubwayLine(String name, Integer distance, SubwayLineColor color, Station upStation, Station downStation) {
        this.name = name;
        this.distance = distance;
        this.color = color;
        this.upStation = upStation;
        this.downStation = downStation;
    }

    public SubwayLine(SubwayLine subwayLine) {
        this.id = subwayLine.getId();
        this.name = subwayLine.getName();
        this.distance = subwayLine.getDistance();
        this.color = subwayLine.getColor();
        this.upStation = subwayLine.getUpStation();
        this.downStation = subwayLine.getDownStation();
        this.stations = subwayLine.getStations();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public SubwayLineColor getColor() {
        return color;
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public Integer getDistance() {
        return distance;
    }

    public List<StationToSubwayLine> getStations() {
        return stations;
    }

    public SubwayLine update(String name, SubwayLineColor color) {
        this.name = name;
        this.color = color;

        return new SubwayLine(this);
    }

    public void performDelete(StationToSubwayLine upStationToSubwayLine, StationToSubwayLine downStationToSubwayLine) {
        this.upStation.removeSubwayLine(upStationToSubwayLine);
        this.downStation.removeSubwayLine(downStationToSubwayLine);
        this.stations.removeAll(List.of(upStationToSubwayLine, downStationToSubwayLine));
        this.upStation = null;
        this.downStation = null;
    }

    public void updateStations(List<StationToSubwayLine> stationToSubwayLines) {
        this.stations.addAll(stationToSubwayLines);
    }
}
