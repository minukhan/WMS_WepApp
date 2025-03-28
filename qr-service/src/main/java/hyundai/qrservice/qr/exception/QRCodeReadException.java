package hyundai.qrservice.qr.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class QRCodeReadException extends RuntimeException {
    public QRCodeReadException() {
        super("QR 코드 읽기 중 오류 발생");
    }
}
