package hyundai.qrservice.qr.application.port.in;

public interface GenerateQRCodeUseCase {
    QRCodeResponse generateQRCode(GenerateQRCodeCommand command);
}
