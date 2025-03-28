package hyundai.storageservice.app.storage.exception;


import hyundai.storageservice.common.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class StorageNotFoundException extends BusinessException {

    public StorageNotFoundException() {
        super("[ERROR] 해당하는 storage가 존재하지 않습니다.");
    }
}
