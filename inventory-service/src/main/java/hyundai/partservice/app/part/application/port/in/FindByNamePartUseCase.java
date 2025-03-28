package hyundai.partservice.app.part.application.port.in;


import hyundai.partservice.app.part.adapter.dto.PartResponse;

public interface FindByNamePartUseCase {
    public abstract PartResponse findByNamePart(String partName);
}
