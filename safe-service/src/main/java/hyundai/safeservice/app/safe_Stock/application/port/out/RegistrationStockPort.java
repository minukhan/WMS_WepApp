package hyundai.safeservice.app.safe_Stock.application.port.out;


import hyundai.safeservice.app.safe_Stock.application.entity.SafeStock;

public interface RegistrationStockPort {

    public abstract void stockSave(SafeStock safeStock);

}
