package hyundai.purchaseservice.purchase.application.exception;

import hyundai.purchaseservice.common.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class RequestExistsException extends BusinessException {
    public RequestExistsException(String message) {
        super("[Error] 당일에 품목 코드 " + message + " 부품 요청서가 이미 존재합니다.");
    }
}
