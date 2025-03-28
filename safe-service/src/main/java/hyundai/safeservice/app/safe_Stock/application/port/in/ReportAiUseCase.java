package hyundai.safeservice.app.safe_Stock.application.port.in;

import hyundai.safeservice.app.safe_Stock.adapter.dto.PartSafeListResponse;

public interface ReportAiUseCase {
    public abstract PartSafeListResponse getReport(int year, int month);
}
