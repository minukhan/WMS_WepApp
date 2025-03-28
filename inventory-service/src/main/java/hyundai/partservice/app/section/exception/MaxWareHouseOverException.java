package hyundai.partservice.app.section.exception;

import hyundai.partservice.common.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MaxWareHouseOverException extends BusinessException {

    public MaxWareHouseOverException() {
        super("[ERROR] 현재 이 부품들을 수용할 공간이 없습니다.");
    }
}
