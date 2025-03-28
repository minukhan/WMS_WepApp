package hyundai.qrservice.qr.application.port.out;

import hyundai.qrservice.qr.application.domain.Qr;

public interface SaveQRCodePort {
    void save(Qr qrCode);
}
