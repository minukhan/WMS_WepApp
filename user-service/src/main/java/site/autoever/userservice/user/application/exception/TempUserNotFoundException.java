package site.autoever.userservice.user.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import site.autoever.userservice.infrastructure.exception.BusinessException;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TempUserNotFoundException extends BusinessException {
    public TempUserNotFoundException() {
        super("[ERROR] 해당하는 임시 사용자 id는 존재하지 않습니다.");
    }

}
