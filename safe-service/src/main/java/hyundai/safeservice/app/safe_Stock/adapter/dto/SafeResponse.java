package hyundai.safeservice.app.safe_Stock.adapter.dto;

import java.util.List;

public record SafeResponse(
        List<SafeFilterDto> safeFilterDtos
) {
    public static SafeResponse of(List<SafeFilterDto> safeFilterDtos) {
        return new SafeResponse(safeFilterDtos);
    }
}
