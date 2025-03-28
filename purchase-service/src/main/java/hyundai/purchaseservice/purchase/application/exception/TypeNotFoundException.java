package hyundai.purchaseservice.purchase.application.exception;

import hyundai.purchaseservice.common.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TypeNotFoundException extends BusinessException {
    public TypeNotFoundException(String type) {
        super("[Error] "+type+" 조회 불가. 가능 항목: 전체, 요청 중, 완료");
    }
}
