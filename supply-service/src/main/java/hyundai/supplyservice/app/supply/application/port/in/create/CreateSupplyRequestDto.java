package hyundai.supplyservice.app.supply.application.port.in.create;

import hyundai.supplyservice.app.supply.application.port.in.commondto.PartCountDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "현대공장에서 생산데이터 요청 생성 커맨드")
public class CreateSupplyRequestDto {

    @NotNull(message = "마감 기한은 필수입니다.")
    @Schema(description = "요청 마감 기한", example = "2024-02-20")
    private final LocalDate deadline;

    @Schema(description = "요청 부품 코드")
    @Builder.Default
    private final List<PartCountDto> parts=new ArrayList<>();


}
