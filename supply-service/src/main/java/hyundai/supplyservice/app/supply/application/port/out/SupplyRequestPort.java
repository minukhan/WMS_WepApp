package hyundai.supplyservice.app.supply.application.port.out;

import hyundai.supplyservice.app.supply.application.entity.SupplyRequest;

import java.time.LocalDate;
import java.util.List;

public interface SupplyRequestPort {
    SupplyRequest save(SupplyRequest supplyRequest);

    // 요청 삭제
    void delete(Long requestId);

    // 요청 상세 조회
    SupplyRequest findById(Long requestId);

    // 요청 상태 변경 및 메시지 추가
    SupplyRequest updateStatus(Long requestId, String status, String message);

    // 이번달 주문수 계산
    List<SupplyRequest> findByDeadlineBetween(LocalDate startDate, LocalDate endDate);
}
