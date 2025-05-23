package hyundai.partservice.app.part.exception;

import hyundai.partservice.common.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class OptimalStockOverZeroException extends BusinessException {

    public OptimalStockOverZeroException() {
        super("[ERROR] 적정 재고 수량은 0보다 낮게 설정할 수 없습니다.");
    }
}
