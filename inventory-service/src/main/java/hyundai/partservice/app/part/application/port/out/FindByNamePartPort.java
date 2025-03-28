package hyundai.partservice.app.part.application.port.out;


import hyundai.partservice.app.part.application.entity.Part;

public interface FindByNamePartPort {
    public abstract Part findByNamePart(String partName);
}
