package hyundai.supplyservice.app.supply.application.service;

import hyundai.supplyservice.app.supply.application.entity.SupplyRequest;
import hyundai.supplyservice.app.supply.application.port.in.delete.DeleteSupplyRequestUsecase;
import hyundai.supplyservice.app.supply.application.port.in.delete.DeleteSupplyResponseDto;
import hyundai.supplyservice.app.supply.application.port.out.SupplyRequestPort;
import hyundai.supplyservice.app.supply.exception.RequestCannotBeDeletedException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class DeleteSupplyRequestService implements DeleteSupplyRequestUsecase {
    private final SupplyRequestPort supplyRequestPort;

    @Transactional
    public DeleteSupplyResponseDto deleteSupplyRequest(Long requestId) {

        // 1. requestId 존재 여부 확인
        SupplyRequest supplyRequest = supplyRequestPort.findById(requestId);

        // 2. WAITING 상태가 대기중일 경우만 삭제 가능
        if (!"WAITING".equals(supplyRequest.getStatus())) {
            throw new RequestCannotBeDeletedException(supplyRequest.getStatus());
        }

        // 3. 삭제 수행
        supplyRequestPort.delete(requestId);


        return new DeleteSupplyResponseDto(requestId,supplyRequest.getStatus(),"삭제 성공!" );



    }

}
