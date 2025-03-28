package hyundai.partservice.app.section.application.service;

import hyundai.partservice.app.section.adapter.dto.SectionPartInventoryDto;
import hyundai.partservice.app.section.adapter.dto.SectionPartInventoryResponse;
import hyundai.partservice.app.section.application.entity.Section;
import hyundai.partservice.app.section.application.port.in.FindByNameSectionPartInfoUseCase;
import hyundai.partservice.app.section.application.port.out.FindByNameSectionPartInfoPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FindByNameSectionPartInfoService implements FindByNameSectionPartInfoUseCase {

    private final FindByNameSectionPartInfoPort findByNameSectionPartInfoPort;
    @Override
    public SectionPartInventoryResponse findByNameSectionPartInfo(String name) {

        List<Section> sections = findByNameSectionPartInfoPort.findByName(name);

        List<SectionPartInventoryDto> sectionPartInventoryDtos = sections.stream()
                .flatMap(section -> section.getInventories().stream()
                        .map(inventory -> SectionPartInventoryDto.of(
                                section.getFloor(),
                                inventory.getPart().getName(),
                                inventory.getPartQuantity()
                        ))
                )
                .toList();

        return SectionPartInventoryResponse.from(sectionPartInventoryDtos);
    }
}
