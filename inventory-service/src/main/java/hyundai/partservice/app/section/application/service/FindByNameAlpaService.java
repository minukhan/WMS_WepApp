package hyundai.partservice.app.section.application.service;

import hyundai.partservice.app.inventory.adapter.dto.InventoryDto;
import hyundai.partservice.app.inventory.adapter.dto.InventoryListResponse;
import hyundai.partservice.app.inventory.adapter.dto.InventoryResponse;
import hyundai.partservice.app.section.adapter.dto.SectionInventoryResponse;
import hyundai.partservice.app.section.application.entity.Section;
import hyundai.partservice.app.section.application.port.in.FindByNameAlpaUseCase;
import hyundai.partservice.app.section.application.port.out.FindByNameAlpaPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FindByNameAlpaService implements FindByNameAlpaUseCase {

    private final FindByNameAlpaPort findByNameAlpaPort;

    @Override
    public InventoryListResponse findByAlpa(String alpa) {

        List<Section> sections = findByNameAlpaPort.findByName(alpa);

        List<InventoryDto> inventoryDtos = sections.stream()
                .flatMap((section -> section.getInventories().stream()
                        .map((inventory -> InventoryDto.from(inventory)))
                )).collect(Collectors.toUnmodifiableList());



        return InventoryListResponse.from(inventoryDtos);
    }
}
