package hyundai.supplyservice.app.supply.exception;

import hyundai.supplyservice.common.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UsersNotFoundException extends BusinessException {
    public UsersNotFoundException(String message) {
        super("유저 정보가 없습니다.");
    }
}
