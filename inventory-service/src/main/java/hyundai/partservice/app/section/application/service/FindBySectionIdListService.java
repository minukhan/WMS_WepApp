package hyundai.partservice.app.section.application.service;

import hyundai.partservice.app.section.adapter.dto.SectionDto;
import hyundai.partservice.app.section.adapter.dto.SectionPurchaseResponse;
import hyundai.partservice.app.section.application.entity.Section;
import hyundai.partservice.app.section.application.port.in.FindBySectionIdListUseCase;
import hyundai.partservice.app.section.application.port.out.FindBySectionIdListPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FindBySectionIdListService implements FindBySectionIdListUseCase {

    private final FindBySectionIdListPort findBySectionIdListPort;

    @Override
    public SectionPurchaseResponse findBySectionIdList(List<Long> sectionIds) {

        List<Section> sections =  findBySectionIdListPort.findBySectionIdList(sectionIds);

        List<SectionDto> sectionDtos = sections.stream()
                .map((section -> SectionDto.from(section)))
                .collect(Collectors.toUnmodifiableList());


        return SectionPurchaseResponse.from(sectionDtos);
    }


}
