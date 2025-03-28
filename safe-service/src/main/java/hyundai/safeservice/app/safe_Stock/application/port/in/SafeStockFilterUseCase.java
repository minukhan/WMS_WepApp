package hyundai.safeservice.app.safe_Stock.application.port.in;


import hyundai.safeservice.app.safe_Stock.adapter.dto.SafeResponse;

public interface SafeStockFilterUseCase {
    public abstract SafeResponse filter(String partId,String searchType, String searchText,String orderType,boolean isDesc);
}
