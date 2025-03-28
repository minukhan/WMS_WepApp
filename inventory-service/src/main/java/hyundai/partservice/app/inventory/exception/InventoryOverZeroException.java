package hyundai.partservice.app.inventory.exception;


import hyundai.partservice.common.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InventoryOverZeroException extends BusinessException {

    public InventoryOverZeroException() {
        super("[ERROR] 재고는 0보다 적어질 수 없습니다.");
    }
}
