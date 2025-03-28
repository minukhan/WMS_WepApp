package hyundai.partservice.app.inventory.exception;


import hyundai.partservice.common.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InventoryPartQuantityEmptyException extends BusinessException {

    public InventoryPartQuantityEmptyException() {
        super("[ERROR] 재고가 존재하지 않습니다. ");
    }
}
