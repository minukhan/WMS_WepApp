package hyundai.purchaseservice.purchase.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record PageInfo(
        @Schema(description = "현재 페이지")
        Integer currentPage,
        @Schema(description = "한 페이지 크기")
        Integer pageSize,
        @Schema(description = "총 페이지 수")
        Integer totalPages,
        @Schema(description = "총 결과 개수")
        Long totalElements,
        @Schema(description = "다음 페이지 존재")
        Boolean hasNext,
        @Schema(description = "이전 페이지 존재")
        Boolean hasPrevious
) {
}
