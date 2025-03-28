package hyundai.partservice.app.part.application.port.out;

import hyundai.partservice.app.part.application.entity.Part;

import java.util.List;

public interface FIndByNameContainPort {

    public abstract List<Part> findByNameContains(String name);
}
