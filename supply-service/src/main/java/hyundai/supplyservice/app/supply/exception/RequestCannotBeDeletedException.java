package hyundai.supplyservice.app.supply.exception;

import hyundai.supplyservice.common.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class RequestCannotBeDeletedException extends BusinessException {
    public RequestCannotBeDeletedException(String status) {
        super("status : "+status+" 삭제불가 상태");
    }
}
