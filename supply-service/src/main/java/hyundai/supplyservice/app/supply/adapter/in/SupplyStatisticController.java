package hyundai.supplyservice.app.supply.adapter.in;

import hyundai.supplyservice.app.supply.application.port.in.commondto.PartCountDto;
import hyundai.supplyservice.app.supply.application.port.in.statistic.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class SupplyStatisticController {
    private final GetStatisticUsecase getStatisticUsecase;


    @PreAuthorize("hasAnyAuthority('ROLE_INFRA_MANAGER','ROLE_ADMIN','ROLE_WAREHOUSE_MANAGER')")
    @GetMapping("supply/monthly-top-parts")
    @Operation(summary = "월별로 출고량 상위 10개 부품 및 전체 출고량 대비 퍼센트 조회",  tags = "대시보드 통계 - 출고")
    public ResponseEntity<MonthlyTopPartsResponseDto> getMonthlyTopParts(
            @Parameter(description = "연도", example = "2025")
            @RequestParam int year,
            @Parameter(description = "월 1~12 사이의 값", example = "2")
            @RequestParam int month) {
        return ResponseEntity.ok(getStatisticUsecase.getMonthlyTopParts(year, month));
    }


    @PreAuthorize("hasAnyAuthority('ROLE_INFRA_MANAGER','ROLE_ADMIN','ROLE_WAREHOUSE_MANAGER')")
    @GetMapping("supply/monthly-category")
    @Operation(summary = "월별 카테고리별 부품 출고율",  tags = "대시보드 통계 - 출고")
    public ResponseEntity<MonthlyCategoryResponseDto> getMonthlyCategory(
            @Parameter(description = "연도", example = "2025")
            @RequestParam int year,
            @Parameter(description = "월 1~12 사이의 값", example = "2")
            @RequestParam int month

    ){
        return ResponseEntity.ok(getStatisticUsecase.getMonthlyCategory(year, month));
    }

    @GetMapping("supply/monthly/total-count")
    @Operation(summary = "한달동안 출고된 부품 박스 수, 접수된 출고 주문 수 및 승인된 주문 수", tags = "보고서 - 출고")
    public ResponseEntity<MonthlyTotalCountResponseDto> getMonthlyTotalCount(
            @Parameter(example = "2025")
            @RequestParam int year,
            @Parameter(example = "2")
            @RequestParam int month
    ){
        return ResponseEntity.ok(getStatisticUsecase.getMonthlyTotalCount(year, month));
    }

    @GetMapping("supply/monthly/part-trends")
    @Operation(summary = "이번달 기준 상위 10개 부품을 지난달과 비교합니다.", tags = "보고서 - 출고")
    public ResponseEntity<CurrentLastMonthTopPartsResponseDto> getCurrentLastMonthTopParts(
            @Parameter(example = "2025")
            @RequestParam int year,
            @Parameter(example = "2")
            @RequestParam int month
    ){
        return ResponseEntity.ok(getStatisticUsecase.getCurrentLastMonthTopParts(year, month));
    }

    @GetMapping("supply/parts/monthly-quantity")
    @Operation(summary = "입력한 달의 전체 부품별 출고량 계산", tags = "부품 담당자 사용하세요^^")
    public ResponseEntity<List<PartCountDto>> getMonthlyTotalQuantityByPart(
            @Parameter(example = "2025")
            @RequestParam int year,
            @Parameter(example = "2")
            @RequestParam int month
    ){
        List<PartCountDto> monthlyPartQuantities = getStatisticUsecase.getMonthlyTotalQuantityByPart(year, month);

        return ResponseEntity.ok(monthlyPartQuantities);
    }

}
