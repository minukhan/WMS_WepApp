package hyundai.safeservice.app.propriety_Stock.application.port.out;

import hyundai.safeservice.app.propriety_Stock.application.entity.ProprietyStock;

import java.util.List;

public interface FilterProprietyPort {
    public abstract List<ProprietyStock> findBypartId(String partId);
}
