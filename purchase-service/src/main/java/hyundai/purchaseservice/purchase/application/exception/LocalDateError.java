package hyundai.purchaseservice.purchase.application.exception;

import hyundai.purchaseservice.common.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class LocalDateError extends BusinessException {
    public LocalDateError(String message) {
        super("[Error] " + message + "는 현재와 같거나 미래여야 합니다.");
    }
}
