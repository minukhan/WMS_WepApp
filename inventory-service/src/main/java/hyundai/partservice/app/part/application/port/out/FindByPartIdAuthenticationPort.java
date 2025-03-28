package hyundai.partservice.app.part.application.port.out;


import hyundai.partservice.app.part.application.entity.Part;

import java.util.List;

public interface FindByPartIdAuthenticationPort {
    public abstract Part findByPartId(String partId);
    public abstract int partCount(String partId);
    public abstract int currentTotalCount();
}
