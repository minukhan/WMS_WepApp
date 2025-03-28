package hyundai.partservice.app.part.exception;

import hyundai.partservice.common.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CategoryPartNotFoundException extends BusinessException {

    public CategoryPartNotFoundException() { super("[ERROR] 해당 Category 의 부품이 존재하지 않습니다.");}

}
