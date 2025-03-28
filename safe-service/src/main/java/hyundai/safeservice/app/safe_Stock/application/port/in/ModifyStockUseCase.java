package hyundai.safeservice.app.safe_Stock.application.port.in;


import hyundai.safeservice.app.safe_Stock.adapter.dto.SafeRequestList;

public interface ModifyStockUseCase {
    public abstract void modifyStock(SafeRequestList safeRequestList);
}
