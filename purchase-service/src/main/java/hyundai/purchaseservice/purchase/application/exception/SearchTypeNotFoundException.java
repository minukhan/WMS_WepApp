package hyundai.purchaseservice.purchase.application.exception;

import hyundai.purchaseservice.common.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SearchTypeNotFoundException extends BusinessException {
    public SearchTypeNotFoundException(String searchType, String allowed) {
        super("[Error] " + searchType + "로 검색할 수 없습니다. 가능한 항목(택1) : "+allowed);
    }
}
