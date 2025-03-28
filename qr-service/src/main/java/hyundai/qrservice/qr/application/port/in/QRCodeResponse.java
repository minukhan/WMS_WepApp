package hyundai.qrservice.qr.application.port.in;

public record QRCodeResponse(Long Long, byte[] imageData) {

    public QRCodeResponse {
        imageData = imageData.clone();
    }

    public byte[] imageData() {
        return imageData.clone();
    }
}
