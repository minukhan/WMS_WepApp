package hyundai.qrservice.qr.application.port.out;

import hyundai.qrservice.qr.application.domain.Qr;

import java.util.Optional;

public interface LoadQRCodePort {
    Optional<Qr> loadById(Long id);
}
