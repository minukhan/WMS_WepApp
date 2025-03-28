package hyundai.partservice.app.section.exception;


import hyundai.partservice.common.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SectionQuantityOverZeroException extends BusinessException {
    public SectionQuantityOverZeroException() {
        super("[ERROR] 0보다 작아질 순 없습니다.");
    }
}
