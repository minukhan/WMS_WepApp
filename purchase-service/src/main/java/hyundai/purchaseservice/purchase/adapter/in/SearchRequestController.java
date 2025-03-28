package hyundai.purchaseservice.purchase.adapter.in;

import hyundai.purchaseservice.common.exception.BusinessExceptionResponse;
import hyundai.purchaseservice.purchase.adapter.in.dto.PurchaseDetailResponse;
import hyundai.purchaseservice.purchase.adapter.in.dto.PurchaseRequestListResponse;
import hyundai.purchaseservice.purchase.application.port.in.SearchRequestUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "부품 요청서 조회 및 검색 API", description = "부품 구매에서 전체, 요청 중, 완료인 요청을 조회하는 api")
public class SearchRequestController {

    private final SearchRequestUseCase searchRequestUseCase;

    @Operation(summary = "전체 부품 요청서 조회", description = "부품 요청서를 조회한다. 검색과 정렬이 가능하다. 검색과 정렬은 각각 한 항목으로만 가능하다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청서 조회 성공.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PurchaseRequestListResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 데이터 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BusinessExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류")})
    @GetMapping("/purchase/search")
    public ResponseEntity<?> searchPurchase(
            @Parameter(description = "현재 페이지(시작 페이지: 1)", example = "1")
            @RequestParam Integer page,

            @Parameter(description = "페이지 크기", example = "10")
            @RequestParam(required = false, defaultValue = "10") Integer size,

            @Parameter(description = "검색 타입(전체, 요청 중, 완료)", example = "요청 중")
            @RequestParam String type,

            @Parameter(description = "검색하려는 항목(품목 코드, 품목 명, 요청자, 납품 회사명 중 택1). searchText 필수.", example = "품목 코드")
            @RequestParam(required = false) String searchType,

            @Parameter(description = "검색하려는 값", example = "PH-1")
            @RequestParam(required = false) String searchText,

            @Parameter(description = "정렬하려는 항목(요청 일시, 수량, 금액 중 택1)", example = "요청 일시")
            @RequestParam(required = false) String orderType,

            @Parameter(description = "오름차순은 false(생략 가능), 내림차순은 true", example = "true")
            @RequestParam(required = false) Boolean isDesc
    ){
        return ResponseEntity.ok(searchRequestUseCase.searchPurchaseRequests(page, size, type, searchType, searchText, orderType, isDesc));
    }


    @Operation(summary = "상세 부품 요청서 조회", description = "요청서 id로 부품 요청서의 상세 내용을 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청서 조회 성공.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PurchaseDetailResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 데이터 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BusinessExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류")})
    @GetMapping("/purchase/detail/{requestId}")
    public ResponseEntity<?> searchPurchaseDetail(
            @Parameter(description = "조회하려는 부품 요청서 id", example = "1")
            @PathVariable Long requestId
    ){
        return ResponseEntity.ok(searchRequestUseCase.searchPurchaseDetails(requestId));
    }
}
