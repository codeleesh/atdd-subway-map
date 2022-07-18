package nextstep.subway.line.application.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class LineUpdateRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String color;
}