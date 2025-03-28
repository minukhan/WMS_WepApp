package hyundai.partservice.app.part.application.port.out;

import hyundai.partservice.app.part.application.entity.Part;

import java.util.List;

public interface FindByIdListPort {

    public abstract List<Part> findByIdList(List<String> idList);
}
