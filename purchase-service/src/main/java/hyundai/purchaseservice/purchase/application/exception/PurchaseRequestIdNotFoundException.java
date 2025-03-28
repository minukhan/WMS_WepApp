package hyundai.purchaseservice.purchase.application.exception;

import hyundai.purchaseservice.common.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PurchaseRequestIdNotFoundException extends BusinessException {
    public PurchaseRequestIdNotFoundException() {
        super("[Error] 해당하는 부품 요청서 id가 없습니다.");
    }
}
