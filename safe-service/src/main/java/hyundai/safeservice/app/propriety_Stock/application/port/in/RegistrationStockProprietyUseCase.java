package hyundai.safeservice.app.propriety_Stock.application.port.in;

import hyundai.safeservice.app.propriety_Stock.adapter.dto.RequestProprietyDto;

public interface RegistrationStockProprietyUseCase {
    public abstract void registerSafeStock(RequestProprietyDto requestProprietyDto);
}
