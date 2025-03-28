package hyundai.purchaseservice.purchase.application.exception;

import hyundai.purchaseservice.common.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class RequestTooManyException extends BusinessException {
    public RequestTooManyException(Integer quantity) {
        super("[Error] 해당 날짜에 " + quantity +"개를 입고할 수 없습니다.");
    }
}
