package nextstep.subway.exception;

public class DuplicateLineException extends RuntimeException {

    private static final String MESSAGE = "노선 이름이 중복 됩니다.";

    public DuplicateLineException() {
        super(MESSAGE);
    }

}