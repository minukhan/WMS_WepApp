package hyundai.supplyservice.app.supply.application.port.in.read;

import hyundai.supplyservice.app.supply.application.port.in.commondto.PartCountPriceDto;
import hyundai.supplyservice.app.supply.application.port.in.commondto.UserInfoDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public record SupplyRequestDetailResponseDTO(

    List<UserInfoDto> userInfo,

    @Schema(description = "주문서 ID", example = "6")
    Long requestId,

    @Schema(description = "주문 상태", example = "WAITING")
    String status,

    @Schema(description = "반려된 경우 사유", example = "반려된 요청의 경우 사유")
    String message,

    @Schema(description = "요청 마감 기한", example = "2024-02-20T23:59:59")
    LocalDate deadline,

    @Schema(description = "주문 생성 날짜", example = "2024-02-10T23:59:59")
    LocalDate requestDate,

    @Schema(description = "주문 부품 리스트")
    List<PartCountPriceDto> parts,

    @Schema(description = "총 부품 개수", example = "5")
    int totalPartQuantity,
    @Schema(description = "전체 주문 금액", example = "50000")
    Long totalAmount
){
    public SupplyRequestDetailResponseDTO {
        userInfo = userInfo == null ? new ArrayList<>() : userInfo;
        parts = parts == null ? new ArrayList<>() : parts;
    }
}
