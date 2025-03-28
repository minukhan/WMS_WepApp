package hyundai.purchaseservice.purchase.application.exception;

import hyundai.purchaseservice.common.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST)
public class InvalidInputException extends BusinessException {
    public InvalidInputException(String message) {
        super("[Error] " + message + "은 유효하지 않은 입력입니다.");
    }
}
