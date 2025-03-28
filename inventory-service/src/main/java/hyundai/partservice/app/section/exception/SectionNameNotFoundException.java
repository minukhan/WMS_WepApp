package hyundai.partservice.app.section.exception;

import hyundai.partservice.common.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class SectionNameNotFoundException extends BusinessException {
    public SectionNameNotFoundException() {
        super("[ERROR] 해당 Name 의 Section이 존재하지 않습니다.");}
}
