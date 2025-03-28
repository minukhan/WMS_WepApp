package hyundai.partservice.app.part.application.port.in;


import hyundai.partservice.app.part.adapter.dto.PartListPurchaseResponse;

import java.util.List;

public interface FindByIdListUseCase {

    public abstract PartListPurchaseResponse FindByIdList(List<String> partIds);
}
