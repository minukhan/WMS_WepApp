package hyundai.partservice.app.part.application.port.in;

public interface FindByIdModifyOptimalStockUseCase {
    public abstract  void modifyOptimal(String partId, int optimalStock);
}
