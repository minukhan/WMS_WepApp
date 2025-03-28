package hyundai.purchaseservice.purchase.application.exception;

import hyundai.purchaseservice.common.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class OrderTypeNotFoundException extends BusinessException {
    public OrderTypeNotFoundException(String orderType, String allowed) {
        super("[Error] " + orderType + "로 정렬할 수 없습니다. 가능한 항목(택1) : "+allowed);
    }
}
