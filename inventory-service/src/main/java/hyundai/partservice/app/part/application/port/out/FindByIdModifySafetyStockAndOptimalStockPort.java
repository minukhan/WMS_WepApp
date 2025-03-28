package hyundai.partservice.app.part.application.port.out;

import hyundai.partservice.app.part.application.entity.Part;

public interface FindByIdModifySafetyStockAndOptimalStockPort {

    public abstract Part findById(String partId);
    public abstract void save(Part part);
}
