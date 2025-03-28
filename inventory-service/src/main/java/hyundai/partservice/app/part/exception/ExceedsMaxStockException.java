package hyundai.partservice.app.part.exception;


import hyundai.partservice.common.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ExceedsMaxStockException extends BusinessException {

    public ExceedsMaxStockException() {
        super("[ERROR] 적정재고는 최대 재고보다 높게 설정할 수 없습니다.");
    }
}
