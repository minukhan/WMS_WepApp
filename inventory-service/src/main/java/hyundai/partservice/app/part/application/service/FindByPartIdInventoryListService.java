package hyundai.partservice.app.part.application.service;


import hyundai.partservice.app.inventory.adapter.dto.InventoryDto;
import hyundai.partservice.app.inventory.adapter.dto.InventorySectionDto;
import hyundai.partservice.app.part.adapter.dto.PartDto;
import hyundai.partservice.app.part.adapter.dto.PartInventoryResponse;
import hyundai.partservice.app.part.application.entity.Part;
import hyundai.partservice.app.part.application.port.in.FIndByPartIdInventoryListUseCase;
import hyundai.partservice.app.part.application.port.out.FIndByPartIdInventoryListPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FindByPartIdInventoryListService implements FIndByPartIdInventoryListUseCase {

    private final FIndByPartIdInventoryListPort findByPartIdInventoryListPort;

    @Override
    public PartInventoryResponse getPartInventory(String partId) {

        Part part =findByPartIdInventoryListPort.findPartByPartIdInventory(partId);

        //Part Dto 변환
        PartDto partDto = PartDto.from(part);

        //Inventory List Dto 변환
        List<InventorySectionDto> InventorySectionDtos = part.getInventoryList()
                .stream()
                .map((inventory -> InventorySectionDto.from(inventory)))
                .collect(Collectors.toList());

        return PartInventoryResponse.of(partDto,InventorySectionDtos);
    }
}
