package hyundai.partservice.app.part.application.service;

import hyundai.partservice.app.part.adapter.dto.PartListPurchaseResponse;
import hyundai.partservice.app.part.adapter.dto.PartSupplierDto;
import hyundai.partservice.app.part.application.entity.Part;
import hyundai.partservice.app.part.application.port.in.FindByIdListUseCase;
import hyundai.partservice.app.part.application.port.out.FindByIdListPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FindByIdListService implements FindByIdListUseCase {

    private final FindByIdListPort findByIdListPort;

    @Override
    public PartListPurchaseResponse FindByIdList(List<String> partIds) {

        List<Part> parts = findByIdListPort.findByIdList(partIds);
        List<PartSupplierDto> partDtos = parts.stream().map((part -> PartSupplierDto.from(part))).collect(Collectors.toList());

        return PartListPurchaseResponse.from(partDtos);
    }
}
