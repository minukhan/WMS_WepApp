package hyundai.partservice.app.part.application.port.out;

import hyundai.partservice.app.part.application.entity.Part;

import java.util.List;

public interface PartAllPort {
    public abstract List<Part> getAllParts();
}
