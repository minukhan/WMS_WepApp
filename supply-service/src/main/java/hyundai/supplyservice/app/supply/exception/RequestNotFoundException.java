package hyundai.supplyservice.app.supply.exception;

import hyundai.supplyservice.common.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RequestNotFoundException extends BusinessException {
    public RequestNotFoundException(String message) {
        super("해당 요청ID은 존재하지 않습니다");
    }
}
