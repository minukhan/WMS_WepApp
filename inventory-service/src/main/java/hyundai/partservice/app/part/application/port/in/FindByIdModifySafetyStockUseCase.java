package hyundai.partservice.app.part.application.port.in;



public interface FindByIdModifySafetyStockUseCase {

    public abstract void modifySafetyStock(String partId, int safetyStock);
}
