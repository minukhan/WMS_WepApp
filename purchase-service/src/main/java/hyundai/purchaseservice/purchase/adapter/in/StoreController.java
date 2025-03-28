package hyundai.purchaseservice.purchase.adapter.in;

import hyundai.purchaseservice.common.exception.BusinessExceptionResponse;
import hyundai.purchaseservice.purchase.adapter.in.dto.AddProcessedQuantityRequest;
import hyundai.purchaseservice.purchase.adapter.in.dto.PurchaseRequestListResponse;
import hyundai.purchaseservice.purchase.application.port.in.StoreUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "입고 API")   //테스트 하고 자동 발주 입력 api 만들고,,, 또 뭐있더라... 입고 위치 지정하는 거 해결하고, 하루에 자동&수동 주문 입고 있을 때 당일 스케줄에 개수 합쳐져서 나오는지 체크
public class StoreController {

    private final StoreUseCase storeUseCase;

    @Operation(summary = "입고 작업 업데이트", description = "현장 근무자가 QR을 찍으면 입고 작업이 완료된 부품 개수가 증가한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "입고 개수 업데이트 성공."),
            @ApiResponse(responseCode = "400", description = "잘못된 데이터 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BusinessExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BusinessExceptionResponse.class)))})
    @PutMapping("/purchase/store/add")
    public ResponseEntity<?> addProcessedQuantity(
            @RequestBody AddProcessedQuantityRequest request
            ){
        storeUseCase.addProcessedQuantity(request.partId(), request.sectionName());
        return ResponseEntity.ok("OK");
    }
}
