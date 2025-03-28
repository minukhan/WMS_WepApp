package hyundai.partservice.app.section.application.service;

import hyundai.partservice.app.inventory.adapter.dto.InventoryDto;
import hyundai.partservice.app.inventory.adapter.dto.InventoryPartDto;
import hyundai.partservice.app.section.adapter.dto.SectionDto;
import hyundai.partservice.app.section.adapter.dto.SectionInventoryPartResponse;
import hyundai.partservice.app.section.adapter.dto.SectionInventoryResponse;
import hyundai.partservice.app.section.application.entity.Section;
import hyundai.partservice.app.section.application.port.in.FindBySectionIdInventoryUseCase;
import hyundai.partservice.app.section.application.port.out.FindBySectionIdInventoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FindBySectionIdInventoryService implements FindBySectionIdInventoryUseCase {

    private final FindBySectionIdInventoryPort findBySectionIdInventoryPort;

    @Override
    public SectionInventoryPartResponse findBySectionId(Long sectionId) {
        Section section = findBySectionIdInventoryPort.findBySectionId(sectionId);

        if (section == null) {
            Section defaultSection = findBySectionIdInventoryPort.findBySectionIds(sectionId);
            return SectionInventoryPartResponse.of(SectionDto.from(defaultSection), List.of());
        }

        SectionDto sectionDto = SectionDto.from(section);

        List<InventoryPartDto> inventoryDtos = section.getInventories().stream()
                .map(InventoryPartDto::from)
                .collect(Collectors.toList());

        return SectionInventoryPartResponse.of(sectionDto, inventoryDtos);
    }
}
