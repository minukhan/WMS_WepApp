package hyundai.supplyservice.app.supply.adapter.in;

import hyundai.supplyservice.app.supply.application.port.in.commondto.PartCountDto;
import hyundai.supplyservice.app.supply.application.port.in.verify.PartCountResponseDto;
import hyundai.supplyservice.app.supply.application.port.in.verify.StatusChangeResponseDto;
import hyundai.supplyservice.app.supply.application.port.in.verify.ValidationPartCountRequestDto;
import hyundai.supplyservice.app.supply.application.port.in.verify.VerifyResultResponseDto;
import hyundai.supplyservice.app.supply.application.service.VerifyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "승인반려 여부 판단")
public class ValidationController {
    private final VerifyService verifyService;


    @PreAuthorize("hasAnyAuthority('ROLE_INFRA_MANAGER','ROLE_ADMIN')")
    @PostMapping("/supply/verify/{requestId}")
    @Operation(summary = "승인/반려 검증 요청")
    public ResponseEntity<VerifyResultResponseDto> verify(@PathVariable Long requestId){
        VerifyResultResponseDto result =  verifyService.verify(requestId);
        return ResponseEntity.ok(result);

    }

    @PostMapping("/supply/stock")
    @Operation(summary = "출고될 부품수 계산 ")
    public PartCountResponseDto getPartCount(@RequestBody ValidationPartCountRequestDto requestDto){
        return verifyService.getPartCount(requestDto);
    }

    //결과가 true일 경우 승인하기
    @PreAuthorize("hasAnyAuthority('ROLE_INFRA_MANAGER','ROLE_ADMIN')")
    @PutMapping("supply/approve/{requestId}")
    @Operation(summary = "주문 승인하기_ 대기상태만 가능")
    public ResponseEntity<StatusChangeResponseDto> approveRequest(@PathVariable Long requestId){
        StatusChangeResponseDto result = verifyService.approveRequest(requestId);
        return ResponseEntity.ok(result);
    }


    //반려하기
    @PreAuthorize("hasAnyAuthority('ROLE_INFRA_MANAGER','ROLE_ADMIN')")
    @PutMapping("supply/reject/{requestId}")
    @Operation(summary = "주문 반려하기 _ 대기상태만 가능")
    public ResponseEntity<StatusChangeResponseDto> rejectRequest(@PathVariable Long requestId){
        StatusChangeResponseDto result = verifyService.rejectRequest(requestId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("supply/parts/total-quantity")
    @Operation(summary = "자동발주를 위한 출고 수량 계산")
    @Tag(name = "입고 담당자 사용하세요^^")
    public ResponseEntity<Long> getTotalSupplyPartQuantity(@RequestParam LocalDate date){
        Long totalQuantity = verifyService.getTotalSupplyPartQuantity(date);
        return ResponseEntity.ok(totalQuantity);
    }

    @PostMapping("supply/parts/outbound-quantity")
    @Operation(summary = "입력된 날짜의 부품 출고 개수, 없으면 0", tags = "입고 담당자 사용하세요^^")
    public ResponseEntity<List<PartCountDto>> getReleasedPartsCountForDate(@RequestBody ValidationPartCountRequestDto requestDto){
        List<PartCountDto> response = verifyService.getReleasedPartsCountForDate(requestDto);
        return ResponseEntity.ok(response);
    }


}
