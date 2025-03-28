package hyundai.partservice.app.part.application.port.in;


import hyundai.partservice.app.part.adapter.dto.PartAuthenticationResponse;
import hyundai.partservice.app.part.adapter.dto.PartIdRequest;

public interface FindByPartIdAuthenticationUseCase {

    public abstract PartAuthenticationResponse findByPartIds(PartIdRequest partIds);
}
