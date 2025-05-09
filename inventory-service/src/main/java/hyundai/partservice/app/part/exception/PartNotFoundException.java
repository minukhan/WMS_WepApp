package hyundai.partservice.app.part.exception;


import hyundai.partservice.common.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PartNotFoundException extends BusinessException {
    public PartNotFoundException() { super("[ERROR] 해당 ID 의 부품이 존재하지 않습니다.");}
}
