package hyundai.storageservice.app.storage_schedule.exception;

import hyundai.storageservice.common.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class StorageScheduleNotFoundException extends BusinessException {

    public StorageScheduleNotFoundException() {
        super("[ERROR] 해당 날짜에는 일정이 없습니다.");
    }
}
