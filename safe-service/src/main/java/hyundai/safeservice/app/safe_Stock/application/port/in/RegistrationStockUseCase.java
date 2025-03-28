package hyundai.safeservice.app.safe_Stock.application.port.in;

import hyundai.safeservice.app.propriety_Stock.adapter.dto.RequestProprietyDto;

public interface RegistrationStockUseCase {
    public abstract void registerSafeStock(RequestProprietyDto requestProprietyDto);
}
