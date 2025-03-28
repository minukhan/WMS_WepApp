package hyundai.purchaseservice.purchase.application.dto;

import hyundai.purchaseservice.purchase.adapter.out.dto.SearchResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record PurchaseRequestInfoDto(
        @Schema(description = "요청서 id", example = "1")
        Long requestId,
        @Schema(description = "품목 코드", example = "BPH170")
        String partId, // 품목 코드
        @Schema(description = "품목 명", example = "동승석")
        String partName, // 품목명
        @Schema(description = "수량(박스)", example = "30")
        Integer quantity, // 수량(박스)
        @Schema(description = "총 금액", example = "10000000")
        Long totalPrice, // 금액
        @Schema(description = "납품 회사명", example = "현대오토에버")
        String supplier,    // 제조사
        @Schema(description = "요청자/주문자", example = "관리자A")
        String requesterName, // 요청자
        @Schema(description = "주문 일자", example = "2025-02-05")
        LocalDate orderedAt// 주문일자
) {
    public static PurchaseRequestInfoDto of(SearchResponse searchResponse, String partName, String supplier) {
        return new PurchaseRequestInfoDto(
                searchResponse.id(),
                searchResponse.partId(),
                partName,
                searchResponse.quantity(),
                searchResponse.totalPrice(),
                supplier,
                searchResponse.requesterName(),
                searchResponse.orderedAt()
        );
    }
}
