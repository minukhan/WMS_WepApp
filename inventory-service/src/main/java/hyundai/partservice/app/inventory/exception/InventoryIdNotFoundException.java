package hyundai.partservice.app.inventory.exception;

import hyundai.partservice.common.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.NOT_FOUND)
public class InventoryIdNotFoundException extends BusinessException {
    public InventoryIdNotFoundException() {
        super("[ERROR] 해당 Id 의 Inventory가 존재하지 않습니다.");
    }
}
