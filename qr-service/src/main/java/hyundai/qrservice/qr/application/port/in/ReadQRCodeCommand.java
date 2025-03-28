package hyundai.qrservice.qr.application.port.in;

import lombok.Getter;

@Getter
public class ReadQRCodeCommand {
    private final byte[] imageData;   // QR 코드 이미지 데이터

    public ReadQRCodeCommand(byte[] imageData) {
        if (imageData == null || imageData.length == 0) {
            throw new IllegalArgumentException("이미지 데이터는 비어있을 수 없습니다.");
        }
        this.imageData = imageData;
    }
}
