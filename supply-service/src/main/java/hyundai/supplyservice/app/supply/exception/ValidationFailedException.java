package hyundai.supplyservice.app.supply.exception;

import hyundai.supplyservice.common.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ValidationFailedException extends BusinessException {
    // VERIFY SERVICE에서 검증 결과 받아오지 못했을 경우
    public ValidationFailedException(String message) {
        super(message);
    }
}
