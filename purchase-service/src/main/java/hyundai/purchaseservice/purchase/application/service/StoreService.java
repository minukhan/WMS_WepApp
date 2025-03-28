package hyundai.purchaseservice.purchase.application.service;

import hyundai.purchaseservice.purchase.adapter.out.dto.ScheduleIdAndQuantityResponse;
import hyundai.purchaseservice.purchase.adapter.out.dto.feign.SSERequest;
import hyundai.purchaseservice.purchase.adapter.out.dto.feign.SectionDto;
import hyundai.purchaseservice.purchase.application.exception.StoreOverException;
import hyundai.purchaseservice.purchase.application.port.in.StoreUseCase;
import hyundai.purchaseservice.purchase.application.port.out.StorePort;
import hyundai.purchaseservice.purchase.application.port.out.feign.PartServiceClient;
import hyundai.purchaseservice.purchase.application.port.out.feign.SSEServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreService implements StoreUseCase {
    private final PartServiceClient partServiceClient;
    private final SSEServiceClient sseServiceClient;
    private final StorePort storePort;

    @Override
    @Transactional
    public void addProcessedQuantity(String partId, String section){
        // 구역-열-층을 sectionName과 floor로 parsing
        String[] sections = section.split("[구역열층 ]+");
        String sectionName = String.join("-", sections[0], sections[1]);
        Integer floor = Integer.parseInt(sections[2]);
        List<SectionDto> sectionDtos = partServiceClient.getSectionInfoWithName(sectionName).sectionDtos();

        Long sectionId = sectionDtos.stream().filter(dto -> dto.floor() == floor)
                .map(SectionDto::sectionId).findFirst().orElse(null);

        //예외처리 : 입고 완료됐는데 더 하려는 경우.
        // 먼저 특정 구역의 특정 id로 status 요청 중이 없는지 확인.
        List<ScheduleIdAndQuantityResponse> response = storePort.checkProcessedQuantity(partId, sectionId);
        if(response.isEmpty()){
            throw new StoreOverException();
        }

        //입고 스케줄에서 작업한 수량이 증가된다.
        ScheduleIdAndQuantityResponse updateSection = response.get(0);
        storePort.addProcessedQuantity(updateSection);

        //part service의 상태도 업데이트 해준다.
        //String status = partServiceClient.incrementInventory(partId, floor, sectionName);

        // 요청서와 연관된 스케줄이 끝났는지 체크하고 업데이트한다.
        storePort.checkAndUpdateRequestStatus(updateSection.requestId());

        sseServiceClient.makeSSE(new SSERequest(sectionName, floor, true));
    }
}
