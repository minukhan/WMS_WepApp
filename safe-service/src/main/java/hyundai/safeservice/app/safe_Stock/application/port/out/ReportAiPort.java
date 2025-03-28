package hyundai.safeservice.app.safe_Stock.application.port.out;

import hyundai.safeservice.app.safe_Stock.adapter.dto.AiResponse;

public interface ReportAiPort {
    AiResponse getPredictedStock(String partId, int currentSales);
}