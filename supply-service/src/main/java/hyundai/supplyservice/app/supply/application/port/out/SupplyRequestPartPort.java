package hyundai.supplyservice.app.supply.application.port.out;

import hyundai.supplyservice.app.supply.application.entity.SupplyRequestPart;

import java.util.List;

public interface SupplyRequestPartPort {
    // 부품 1개 저장
    SupplyRequestPart save(SupplyRequestPart supplyRequestPart);

    // 부품 리스트 저장
    List<SupplyRequestPart> saveAll(List<SupplyRequestPart> supplyRequestParts);
}
