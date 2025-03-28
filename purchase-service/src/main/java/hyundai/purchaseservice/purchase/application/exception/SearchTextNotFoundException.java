package hyundai.purchaseservice.purchase.application.exception;

import hyundai.purchaseservice.common.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SearchTextNotFoundException extends BusinessException {
    public SearchTextNotFoundException() {
        super("[Error] 검색할 내용이 필요합니다.");
    }
}
