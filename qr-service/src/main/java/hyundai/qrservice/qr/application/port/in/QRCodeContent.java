package hyundai.qrservice.qr.application.port.in;

import lombok.Getter;

@Getter
public class QRCodeContent {

    private final String name;    // QR 코드에서 읽어낸 부품 이름

    public QRCodeContent(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("부품 이름이 없습니다.");
        }
        this.name = name;
    }
}
