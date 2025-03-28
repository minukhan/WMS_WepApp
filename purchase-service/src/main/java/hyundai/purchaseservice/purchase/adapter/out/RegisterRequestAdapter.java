package hyundai.purchaseservice.purchase.adapter.out;

import hyundai.purchaseservice.infrastructure.mapper.PurchaseMapper;
import hyundai.purchaseservice.infrastructure.mapper.ScheduleMapper;
import hyundai.purchaseservice.purchase.adapter.out.dto.SaveRequestDto;
import hyundai.purchaseservice.purchase.adapter.out.dto.SaveScheduleDto;
import hyundai.purchaseservice.purchase.adapter.out.dto.StoreDeliveryAmountBySectionIdDto;
import hyundai.purchaseservice.purchase.application.port.out.RegisterRequestPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RegisterRequestAdapter implements RegisterRequestPort {
    private final PurchaseMapper purchaseMapper;
    private final ScheduleMapper scheduleMapper;

    @Override
    @Transactional
    public void save(SaveRequestDto saveDto, List<List<Long>> scheduleList) {
        // mybatis로 요청서 내용 저장
        purchaseMapper.saveRequest(saveDto);


        // section과 수량을 리스트로 오는 걸로 변경해서 리스트 길이만큼 저장.
        List<SaveScheduleDto> saveScheduleDto = scheduleList.stream()
                .map(schedule -> SaveScheduleDto.of(saveDto, schedule.get(0), schedule.get(1).intValue()))
                .toList();
        scheduleMapper.registerSchedule(saveScheduleDto);
    }

    @Override
    public Long checkRequestExist(String partId, LocalDate when) {
        return purchaseMapper.checkRequestExist(partId, when);
    }

    @Override
    @Transactional
    public List<StoreDeliveryAmountBySectionIdDto> storeUntilArrival(List<Long> sectionIds, LocalDate to) {
        return scheduleMapper.storeAmountUntilArrivalDate(sectionIds, to);
    }

    @Override
    public List<StoreDeliveryAmountBySectionIdDto> allSectionStoreUntilArrival(LocalDate to) {
        return scheduleMapper.storeAmountUntilArrivalDateBySection(to);
    }

    @Override
    public Long getTotalStoreAmountUntilArrivalDate(LocalDate to) {
        return scheduleMapper.getTotalStoreAmountUntilArrivalDate(to);
    }
}
