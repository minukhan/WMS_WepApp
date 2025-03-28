package hyundai.supplyservice.app.supply.adapter.out;

import hyundai.supplyservice.app.infrastructure.repository.SupplyRequestPartRepository;
import hyundai.supplyservice.app.supply.application.entity.SupplyRequestPart;
import hyundai.supplyservice.app.supply.application.port.out.SupplyRequestPartPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SupplyRequestPartAdapter implements SupplyRequestPartPort {

    //주문에 대한 부품 저장
    private final SupplyRequestPartRepository supplyRequestPartJpaRepository;


    @Override
    public SupplyRequestPart save(SupplyRequestPart supplyRequestPart) {
        return supplyRequestPartJpaRepository.save(supplyRequestPart);

    }

    @Override
    public List<SupplyRequestPart> saveAll(List<SupplyRequestPart> supplyRequestParts) {
        return supplyRequestPartJpaRepository.saveAll(supplyRequestParts);
    }


}
