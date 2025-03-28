package hyundai.safeservice.app.propriety_Stock.application.port.out;


import hyundai.safeservice.app.propriety_Stock.application.entity.ProprietyStock;
import hyundai.safeservice.app.safe_Stock.application.entity.SafeStock;

public interface RegistrationStockProprietyPort {

    public abstract void stockSave(ProprietyStock proprietyStock);

}
