package subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import subway.AcceptanceTest;

import static subway.fixture.TestFixtureLine.*;
import static subway.fixture.TestFixtureSection.*;
import static subway.fixture.TestFixtureStation.지하철역_생성_요청;

@DisplayName("지하철 노선 기능 인수 테스트")
class LineSectionAcceptanceTest extends AcceptanceTest {

    private Long 강남역;
    private Long 잠실역;
    private Long 검암역;
    private Long 몽총토성역;
    private Long 이호선;

    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_생성_요청("강남역");
        잠실역 = 지하철역_생성_요청("잠실역");
        검암역 = 지하철역_생성_요청("검암역");
        몽총토성역 = 지하철역_생성_요청("몽총토성역");
        이호선 = 지하철노선_생성_요청("2호선", "bg-green-600", 강남역, 잠실역, 10);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When  지하철 노선 구간을 추가하면
     * Then  지하철 노선 구간 목록 조회 시 생성한 구간을 찾을 수 있다
     */
    @DisplayName("노선 구간을 추가 등록한다.")
    @Test
    void addSection() {

        final ExtractableResponse<Response> 이호선_응답 = 지하철_노선_구간_추가_요청(이호선, 잠실역 ,검암역, 10);

        지하철_노선_추가됨(이호선_응답);

        final ExtractableResponse<Response> 지하철_노선_구간_목록_조회_응답 = 지하철_노선_구간_목록_조회_요청(이호선);

        지하철_노선_구간_목록_중_등록_구간_조회됨(지하철_노선_구간_목록_조회_응답, 2);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When  지하철 노선 구간의 등록되지 않은 상행종점역을 추가하여 구간 등록하면
     * Then  지하철 노선 구간 추가는 실패한다.
     */
    @DisplayName("노선 구간 추가 시 상행종점역은 노선에 등록되어 있지 않아서 구간 등록이 불가능하다.")
    @Test
    void error_addSection() {

        final ExtractableResponse<Response> 이호선_응답 = 지하철_노선_구간_추가_요청(이호선, 검암역 ,몽총토성역, 10);

        지하철_노선_구간_추가_실패됨(이호선_응답, HttpStatus.NOT_FOUND, "노선에 등록된 하행종점역이 없습니다.");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When  지하철 노선 구간의 이미 등록된 하행종점역을 추가하여 구간 등록하면
     * Then  지하철 노선 구간 추가는 실패한다.
     */
    @DisplayName("노선 구간 추가 시 하행종점역은 노선에 등록되어 있어서 구간 등록이 불가능하다.")
    @Test
    void error_addSection_2() {

        final ExtractableResponse<Response> 이호선_응답 = 지하철_노선_구간_추가_요청(이호선, 검암역 ,잠실역, 10);

        지하철_노선_구간_추가_실패됨(이호선_응답, HttpStatus.NOT_FOUND, "노선에 등록된 하행종점역이 없습니다.");
    }

    /**
     * Given 지하철 노선을 생성하고 1개의 구간을 추가 등록하고
     * When  등록된 지하철 노선 구간 1개를 삭제하면
     * Then  해당 노선 구간 정보는 삭제된다
     */
    @DisplayName("노선 구간을 제거한다.")
    @Test
    void removeSection() {

        final ExtractableResponse<Response> 이호선_응답 = 지하철_노선_구간_추가_요청(이호선, 잠실역 ,검암역, 10);

        지하철_노선_추가됨(이호선_응답);

        final ExtractableResponse<Response> 지하철_노선_구간_삭제_응답 = 지하철_노선_구간_삭제_요청(이호선, 검암역);

        지하철_노선_구간_삭제됨(지하철_노선_구간_삭제_응답);

        final ExtractableResponse<Response> 지하철_노선_구간_목록_조회_응답 = 지하철_노선_구간_목록_조회_요청(이호선);

        지하철_노선_구간_목록_중_등록_구간_조회됨(지하철_노선_구간_목록_조회_응답, 1);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When  등록된 지하철 노선 구간 1개를 삭제하면
     * Then  지하철 노선 구간 추가는 실패한다.
     */
    @DisplayName("노선 구간 제거 시 구간이 하나인 경우 삭제 불가로 구간 삭제가 불가능하다.")
    @Test
    void error_removeSection() {

        final ExtractableResponse<Response> 이호선_응답 = 지하철_노선_구간_삭제_요청(이호선, 잠실역);

        지하철_노선_구간_삭제_실패됨(이호선_응답, HttpStatus.BAD_REQUEST, "구간이 1개인 경우 삭제할 수 없습니다.");
    }

    @DisplayName("노선 구간을 제거 시 노선에 등록된 역이 아니어서 구간 삭제가 불가능하다.")
    @Test
    void error_removeSection_2() {

        final ExtractableResponse<Response> 이호선_응답 = 지하철_노선_구간_삭제_요청(이호선, 검암역);

        지하철_노선_구간_삭제_실패됨(이호선_응답, HttpStatus.BAD_REQUEST, "구간이 1개인 경우 삭제할 수 없습니다.");
    }

    @DisplayName("노선 구간을 제거 시 등록된 하행 종점역이 아니어서 구간 삭제가 불가능하다.")
    @Test
    void error_removeSection_3()
    {
        final ExtractableResponse<Response> 이호선_응답 = 지하철_노선_구간_추가_요청(이호선, 잠실역 ,검암역, 10);

        지하철_노선_추가됨(이호선_응답);

        final ExtractableResponse<Response> 이호선_구간_삭제_응답 = 지하철_노선_구간_삭제_요청(이호선, 강남역);

        지하철_노선_구간_삭제_실패됨(이호선_구간_삭제_응답, HttpStatus.NOT_FOUND, "해당 역으로 등록된 마지막 구간이 존재하지 않습니다.");
    }
}