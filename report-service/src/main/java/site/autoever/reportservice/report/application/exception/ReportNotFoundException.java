package site.autoever.reportservice.report.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import site.autoever.reportservice.report.exception.BusinessException;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ReportNotFoundException extends BusinessException {
    public ReportNotFoundException() {
        super("[ERROR] 해당하는 연 월의 보고서는 존재하지 않습니다.");
    }
}
