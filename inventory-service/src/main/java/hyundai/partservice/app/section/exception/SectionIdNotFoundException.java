package hyundai.partservice.app.section.exception;

import hyundai.partservice.common.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SectionIdNotFoundException extends BusinessException {
    public SectionIdNotFoundException() {
        super("[ERROR] 해당 Id 의 Section이 존재하지 않습니다.");
    }
}
