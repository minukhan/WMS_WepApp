package hyundai.supplyservice.app.supply.exception;

import hyundai.supplyservice.common.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AtLeastOnePartRequiredException extends BusinessException {
    public AtLeastOnePartRequiredException() {

        super("적어도 한개 이상의 부품을 입력하세요");
    }
}
