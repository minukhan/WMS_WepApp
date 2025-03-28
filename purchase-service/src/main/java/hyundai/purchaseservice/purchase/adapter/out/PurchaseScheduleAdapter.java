package hyundai.purchaseservice.purchase.adapter.out;

import hyundai.purchaseservice.infrastructure.mapper.ScheduleMapper;
import hyundai.purchaseservice.purchase.adapter.out.dto.GetProgressPercentResponse;
import hyundai.purchaseservice.purchase.adapter.out.dto.GetScheduleDto;
import hyundai.purchaseservice.purchase.adapter.out.dto.GetScheduleResponse;
import hyundai.purchaseservice.purchase.adapter.out.dto.PartIdAndQuantityResponse;
import hyundai.purchaseservice.purchase.application.port.out.PurchaseSchedulePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class PurchaseScheduleAdapter implements PurchaseSchedulePort {
    private final ScheduleMapper scheduleMapper;

    @Override
    public List<PartIdAndQuantityResponse> getPartIds(List<String> partIds, LocalDate from, LocalDate to) {
        return scheduleMapper.getPartIdAndQuantity(partIds, from, to);
    }

    @Override
    public Set<LocalDate> getDates(Integer year, Integer month) {
        return scheduleMapper.getScheduleDates(year, month);
    }

    @Override
    public List<GetScheduleResponse> getMonthSchedule(Integer year, Integer month) {
        return scheduleMapper.getSchedules(year, month);
    }

    @Override
    public List<GetScheduleResponse> getDaySchedule(GetScheduleDto getScheduleDto) {
        return scheduleMapper.getDayScheduleSearch(getScheduleDto);
    }

    @Override
    public Long getDayScheduleTotal(String searchType, String searchText) {
        return scheduleMapper.getDayScheduleSearchCount(searchType, searchText);
    }

    @Override
    public List<GetScheduleResponse> getDayScheduleWithPartIds(GetScheduleDto getScheduleDto) {
        return scheduleMapper.getDayScheduleWithPartIds(getScheduleDto);
    }

    @Override
    public Long getDayScheduleTotalPartIds(List<String> partIds) {
        return scheduleMapper.getDayScheduleCountWithPartIds(partIds);
    }

    @Override
    public List<GetScheduleResponse> getDayScheduleWithSectionIds(GetScheduleDto getScheduleDto) {
        return scheduleMapper.getDayScheduleWithSectionIds(getScheduleDto);
    }

    @Override
    public Long getDayScheduleTotalSectionIds(List<Long> sectionIds) {
        return scheduleMapper.getDayScheduleCountWithSectionIds(sectionIds);
    }

    @Override
    public List<GetProgressPercentResponse> getTomorrowSchedule() {
        return scheduleMapper.getTomorrowSchedule();
    }
}
