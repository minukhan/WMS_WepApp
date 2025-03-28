package hyundai.supplyservice.app.supply.application.port.in.create;

import hyundai.supplyservice.app.supply.application.port.in.commondto.PartInfoDto;

import java.util.List;

public interface CreateSupplyRequestUsecase {
    CreateSupplyRequestResponseDto createSupplyRequest(CreateSupplyRequestDto command);


    List<PartInfoDto> getAllPartsInfo();
}
