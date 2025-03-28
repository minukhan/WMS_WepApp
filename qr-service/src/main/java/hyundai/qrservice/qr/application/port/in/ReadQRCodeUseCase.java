package hyundai.qrservice.qr.application.port.in;

public interface ReadQRCodeUseCase {
    QRCodeContent readQRCode(ReadQRCodeCommand command);
}
