package hyundai.partservice.app.inventory.exception;


import hyundai.partservice.common.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class InventorySectionFloorAndSectionNameAndPartIdNotFoundException extends BusinessException {
    public InventorySectionFloorAndSectionNameAndPartIdNotFoundException() {super("[ERROR] 해당 floor, sectonName, partId 와 일치하는 재고가 없습니다.");}
}
