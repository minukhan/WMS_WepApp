package hyundai.partservice.app.part.exception;

import hyundai.partservice.common.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BelowSafetyStockException extends BusinessException {

    public BelowSafetyStockException() {
        super("[ERROR] 적정재고 설정은 안전재고보다 내려갈 수 없습니다.");
    }
}
