package hyundai.supplyservice.app.supply.application.port.in.commondto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "공급 요청 부품 커맨드")
public class SupplyRequestPartDto {
    @NotNull(message = "품목코드 필수")
    @Schema(description = "품목코드", example = "SHP006")
    private final String partId;

    @NotNull(message = "수량은 필수입니다.")
    @Schema(description = "요청 수량", example = "10")
    private final int quantity;

    @Schema(description = "부품 이름", example = "가니시 램프")
    private final String partName;



}
