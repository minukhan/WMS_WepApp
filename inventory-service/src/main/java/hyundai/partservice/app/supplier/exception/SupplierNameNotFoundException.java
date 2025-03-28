package hyundai.partservice.app.supplier.exception;


import hyundai.partservice.common.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SupplierNameNotFoundException extends BusinessException {
    public SupplierNameNotFoundException() {
        super("[ERROR] 해당 Name 의 Supplier가 존재하지 않습니다.");
    }
}
