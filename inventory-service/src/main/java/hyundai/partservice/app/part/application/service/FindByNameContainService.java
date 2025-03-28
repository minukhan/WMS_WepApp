package hyundai.partservice.app.part.application.service;


import hyundai.partservice.app.part.adapter.dto.PartListPurchaseResponse;
import hyundai.partservice.app.part.adapter.dto.PartSupplierDto;
import hyundai.partservice.app.part.application.entity.Part;
import hyundai.partservice.app.part.application.port.in.FindByNameContainUseCase;
import hyundai.partservice.app.part.application.port.out.FIndByNameContainPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FindByNameContainService implements FindByNameContainUseCase {

    private final FIndByNameContainPort findByNameContainPort;

    @Override
    public PartListPurchaseResponse findByNameContains(String name) {

        List<Part> parts = findByNameContainPort.findByNameContains(name);
        //Dto 로 변환
        List<PartSupplierDto> partSupplierDtos = parts.stream().map((part -> PartSupplierDto.from(part))).collect(Collectors.toList());

        //Response 로 변환 후 리턴
        return PartListPurchaseResponse.from(partSupplierDtos);
    }
}
