package hyundai.safeservice.app.safe_Stock.application.port.out;


import hyundai.safeservice.app.propriety_Stock.application.entity.ProprietyStock;
import hyundai.safeservice.app.safe_Stock.application.entity.SafeStock;

public interface ModifyStockPort {
    public abstract void save(SafeStock safeStock);
    public abstract void savePro(ProprietyStock proprietyStock);
}
