package site.autoever.reportservice.report.application.domain;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import site.autoever.reportservice.report.application.dto.parts.PartInfoWithAIDto;
import site.autoever.reportservice.report.application.dto.purchase.part1.PurchaseSummaryDto;
import site.autoever.reportservice.report.application.dto.supply.SupplyPartInfoDto;
import site.autoever.reportservice.report.application.dto.purchase.PurchasePartAutoStockInfoDto;
import site.autoever.reportservice.report.application.dto.purchase.PurchasePartExpenseInfoDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.autoever.reportservice.report.application.dto.supply.part1.SupplySummaryDto;

@Document(collection = "reports")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Report {

    @Id
    private String id;

    // 생성 시각 등 메타 데이터
    private Instant createdAt;

    private boolean isModified; // false

    // 보고서 기준 (예: 연도, 월)
    private int year;
    private int month;

    // 1. Part 1: 요약
    private PurchaseSummaryDto purchaseSummary;
    private SupplySummaryDto supplySummary;

    // 2. Part 2: 그래프 데이터 (각 항목은 리스트로 저장)
    // 2-1. 출고량 상위 10개 그래프
    private List<SupplyPartInfoDto> supplyQuantityGraph;

    // 2-2. 자동발주 상위 10개 그래프
    private List<PurchasePartAutoStockInfoDto> purchaseAutoStockGraph;

    // 2-3. 지출비 상위 10개 그래프
    private List<PurchasePartExpenseInfoDto> purchaseExpensesGraph;

    // 2-4. ai 추천 총 부품 그래프
    private List<PartInfoWithAIDto> partWithAiGraph;

}
