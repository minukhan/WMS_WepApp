package hyundai.supplyservice.app.supply.application.port.in.read;

import hyundai.supplyservice.app.supply.application.port.in.commondto.PageableResponse;
import org.springframework.data.domain.Pageable;

public interface ReadSupplyRequestUsecase {
    SupplyRequestDetailResponseDTO getRequestDetail(Long requestId);

    //주문서 목록 조회, 페이지네이션
    PageableResponse<SupplyRequestInfoDto> getSupplyRequestList(String status, String name, Pageable pageable);

}
