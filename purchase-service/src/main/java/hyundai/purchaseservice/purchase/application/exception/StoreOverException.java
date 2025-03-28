package hyundai.purchaseservice.purchase.application.exception;

import hyundai.purchaseservice.common.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class StoreOverException extends BusinessException {
    public StoreOverException() {
        super("[Error] 일정에 없거나 작업이 완료된 부품입니다.");
    }
}
