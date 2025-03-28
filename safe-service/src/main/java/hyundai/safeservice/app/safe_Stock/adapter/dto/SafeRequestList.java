package hyundai.safeservice.app.safe_Stock.adapter.dto;

import java.util.List;

public record SafeRequestList(
        List<SafeRequest> safeRequests
) {
    public static SafeRequestList from(List<SafeRequest> safeRequests) {
        return new SafeRequestList(safeRequests);
    }
}
