package hyundai.partservice.app.inventory.exception;

import hyundai.partservice.common.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InventoryOverCapacityException extends BusinessException {
    public InventoryOverCapacityException() {
        super("[ERROR] 현재 구역은 꽉 차있습니다. 다른 구역을 선택해주세요");
    }
}
