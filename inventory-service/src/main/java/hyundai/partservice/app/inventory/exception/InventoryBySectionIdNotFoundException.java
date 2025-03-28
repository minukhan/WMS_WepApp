package hyundai.partservice.app.inventory.exception;


import hyundai.partservice.common.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class InventoryBySectionIdNotFoundException extends BusinessException {

    public InventoryBySectionIdNotFoundException() {
        super("[ERROR] 해당되는 구역에 부품 재고들이 존재하지 않습니다.");
    }
}
