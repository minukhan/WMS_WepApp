package hyundai.partservice.app.section.application.service;

import hyundai.partservice.app.section.adapter.dto.SectionDto;
import hyundai.partservice.app.section.adapter.dto.SectionFindPositionRequest;
import hyundai.partservice.app.section.adapter.dto.SectionFindPositionResponse;
import hyundai.partservice.app.section.adapter.dto.SectionQuantityDto;
import hyundai.partservice.app.section.application.entity.Section;
import hyundai.partservice.app.section.application.port.in.FindStorePositionUseCase;
import hyundai.partservice.app.section.application.port.out.FindStorePositionPort;
import hyundai.partservice.app.section.exception.MaxWareHouseOverException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FindStorePositionService implements FindStorePositionUseCase {

    private final FindStorePositionPort findStorePositionPort;

    @Override
    public SectionFindPositionResponse findStorePosition(SectionFindPositionRequest request) {

        List<Section> sections = findStorePositionPort.findAllSections();

        // 잔여 공간이 큰 순으로 정렬 (내림차순)
        sections.sort((s1, s2) ->
                Integer.compare((s2.getMaxCapacity() - s2.getQuantity()), (s1.getMaxCapacity() - s1.getQuantity()))
        );

        int requiredQuantity = request.quantity();
        List<SectionQuantityDto> allocatedSections = new ArrayList<>();

        for (Section section : sections) {
            if (requiredQuantity <= 0) break; // 필요한 수량을 다 배정하면 종료

            int availableSpace = section.getMaxCapacity() - section.getQuantity();

            if (availableSpace > 0) {
                int allocatedAmount = Math.min(requiredQuantity, availableSpace); // 가능한 만큼 할당
                requiredQuantity -= allocatedAmount;

                // 섹션 정보 저장 (할당된 수량과 함께 응답할 수도 있음)
                allocatedSections.add(SectionQuantityDto.from(section, allocatedAmount));
            }
        }

        if (requiredQuantity!=0) {
            throw new MaxWareHouseOverException();
        }

        return SectionFindPositionResponse.of(allocatedSections, request.partId());
    }
}
