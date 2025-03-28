package site.autoever.userservice.user.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import site.autoever.userservice.infrastructure.exception.BusinessException;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserRoleNotFoundException extends BusinessException {

    public UserRoleNotFoundException() {
        super("[ERROR] 해당하는 사용자 역할은 존재하지 않습니다.");
    }
}
