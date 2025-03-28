package hyundai.partservice.app.part.exception;

import hyundai.partservice.common.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ExceedsOptimalStockException extends BusinessException {

    public ExceedsOptimalStockException() {
        super("[ERROR] 안전 재고는 적정재고를 넘을 수 없습니다.");
    }
}
