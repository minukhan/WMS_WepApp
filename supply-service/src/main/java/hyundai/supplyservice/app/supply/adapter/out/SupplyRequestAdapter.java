package hyundai.supplyservice.app.supply.adapter.out;

import hyundai.supplyservice.app.infrastructure.repository.SupplyRequestRepository;
import hyundai.supplyservice.app.supply.application.entity.SupplyRequest;
import hyundai.supplyservice.app.supply.application.entity.SupplyRequestPart;
import hyundai.supplyservice.app.supply.application.port.out.SupplyRequestPort;
import hyundai.supplyservice.app.supply.exception.RequestNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SupplyRequestAdapter implements SupplyRequestPort{

    // 주문 저장
    private final SupplyRequestRepository supplyRequestJpaRepository;

    @Override
    public SupplyRequest save(SupplyRequest supplyRequest) {
        return supplyRequestJpaRepository.save(supplyRequest);

    }

    @Override
    public void delete(Long id){
        supplyRequestJpaRepository.deleteById(id);
    }

    @Override
    public SupplyRequest findById(Long requestId) {
        return supplyRequestJpaRepository.findById(requestId)
                .orElseThrow(() -> new RequestNotFoundException("해당 요청은 찾을 수 없습니다"));
    }



    @Override
    public SupplyRequest updateStatus(Long requestId, String status, String message){
        SupplyRequest supplyRequest = findById(requestId);
        supplyRequest.updateStatus(status, message);
        return supplyRequestJpaRepository.save(supplyRequest);
    }

    @Override
    public List<SupplyRequest> findByDeadlineBetween(LocalDate startDate, LocalDate endDate) {
        return supplyRequestJpaRepository.findByDeadlineBetween(startDate, endDate);

    }








}
