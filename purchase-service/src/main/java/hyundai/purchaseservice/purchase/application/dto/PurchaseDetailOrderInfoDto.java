package hyundai.purchaseservice.purchase.application.dto;

import hyundai.purchaseservice.purchase.adapter.out.dto.SearchResponse;
import hyundai.purchaseservice.purchase.adapter.out.dto.feign.PartWithSupplierDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record PurchaseDetailOrderInfoDto(
        @Schema(description = "주문자", example = "ㅇㅇㅇ 관리자 / 자동")
        String requesterName,
        @Schema(description = "주문 일시", example = "2025-02-01")
        LocalDate orderedAt,
        @Schema(description = "마감 일시", example = "2025-02-03")
        LocalDate deadline,
        @Schema(description = "납품 업체명", example = "현대")
        String supplierName,
        @Schema(description = "납품 업체 연락처", example = "010-1234-5678")
        String supplierPhone,
        @Schema(description = "납품 업체 주소지", example = "경기도 ...")
        String supplierAddress,
        @Schema(description = "총 금액", example = "3000000")
        Long totalPrice

) {
        public static PurchaseDetailOrderInfoDto of(SearchResponse searchResponse, PartWithSupplierDto part) {
                return new PurchaseDetailOrderInfoDto(
                        searchResponse.requesterName(),
                        searchResponse.orderedAt(),
                        searchResponse.deadline(),
                        part.supplierName(),
                        part.supplierPhoneNumber(),
                        part.supplierAddress(),
                        searchResponse.totalPrice()
                );
        }
}