package hyundai.purchaseservice.purchase.adapter.in;

import hyundai.purchaseservice.common.exception.BusinessExceptionResponse;
import hyundai.purchaseservice.purchase.adapter.in.dto.ExpectedQuantityRequest;
import hyundai.purchaseservice.purchase.adapter.in.dto.ExpectedQuantityResponse;
import hyundai.purchaseservice.purchase.adapter.in.dto.ExpectedQuantityTomorrowResponse;
import hyundai.purchaseservice.purchase.application.port.in.ExpectedQuantityUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@Tag(name = "입고 예정 수량 API", description = "현재부터 due date까지 품목이 입고되는 수량을 계산한다.")
public class ExpectedQuantityController {

    private final ExpectedQuantityUseCase expectedQuantityUseCase;

    @Operation(summary = "입고 예정 수량 조회", description = "각 품목의 입고 예정 수량과 총 입고 예정 수량을 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청서 조회 성공.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExpectedQuantityResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 데이터 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BusinessExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BusinessExceptionResponse.class)))})
    @PostMapping("/purchase/stock")
    public ResponseEntity<?> getExpectedStockQuantity(
            @RequestBody ExpectedQuantityRequest request
    ) {
        return ResponseEntity.ok(expectedQuantityUseCase.prefixSumStockAmount(request));
    }

    @Operation(summary = "다음날 입고 예정 품목 조회", description = "현재 날짜로부터 다음날 입고가 예정된 품목 명, 수량, 위치를 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청서 조회 성공.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExpectedQuantityTomorrowResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 데이터 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BusinessExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BusinessExceptionResponse.class)))})
    @GetMapping("/purchase/stock/tomorrow")
    public ResponseEntity<ExpectedQuantityTomorrowResponse> getExpectedStockQuantityTomorrow(){
        return ResponseEntity.ok(expectedQuantityUseCase.getTomorrowStockList());
    }
}
