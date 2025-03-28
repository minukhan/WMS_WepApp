package hyundai.safeservice.app.propriety_Stock.application.port.in;

import hyundai.safeservice.app.safe_Stock.adapter.dto.SafeResponse;

public interface FilterProprietyUseCase {
    public abstract SafeResponse filter(String partId,String searchType, String searchText, String orderType, boolean isDesc);
}
