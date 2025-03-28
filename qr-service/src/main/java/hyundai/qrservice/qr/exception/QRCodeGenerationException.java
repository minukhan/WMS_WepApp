package hyundai.qrservice.qr.exception;

import hyundai.qrservice.common.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class QRCodeGenerationException extends BusinessException {
    public QRCodeGenerationException() {
        super("QR 코드 생성 중 오류 발생");
    }
}
