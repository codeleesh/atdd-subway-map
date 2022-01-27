package nextstep.subway.domain;

import nextstep.subway.domain.exception.CannotAddSectionException;
import nextstep.subway.domain.exception.CannotRemoveSectionException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    @OrderBy("id ASC")
    private List<Section> sections = new ArrayList<>();

    public void add(Section section) {
        if (!sections.isEmpty()) {
            addableUpStation(section);
            addableDownStation(section);
        }
        sections.add(section);
    }

    private void addableUpStation(Section section) {
        Section lastSection = getLastSection();

        if (!lastSection.isAddableLastSection(section)) {
            String lastStationName = lastSection.downStationName();
            String addUpStationName = section.upStationName();
            throw new CannotAddSectionException(lastStationName, addUpStationName);
        }
    }

    private Section getLastSection() {
        return sections.get(sections.size() - 1);
    }

    private void addableDownStation(Section addSection) {
        Station downStation = addSection.getDownStation();
        boolean containsStation = sections.stream()
                .anyMatch(section -> section.containsStation(downStation));
        if (containsStation) {
            throw new CannotAddSectionException(addSection.downStationName());
        }
    }

    public void remove(Station station) {
        validateSectionsSize();
        validateLastStation(station);

        sections.remove(getLastSection());
    }

    private void validateSectionsSize() {
        if (sections.isEmpty() || sections.size() == 1) {
            throw new CannotRemoveSectionException();
        }
    }

    private void validateLastStation(Station station) {
        if (!getLastSection().isDownStation(station)) {
            throw new CannotRemoveSectionException(station.getName());
        }
    }

    public List<Station> stations() {
        List<Station> stations = sections.stream()
                .map(Section::getUpStation)
                .collect(Collectors.toList());
        stations.add(getLastSection().getDownStation());

        return Collections.unmodifiableList(stations);
    }
}
