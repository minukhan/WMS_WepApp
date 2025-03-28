package hyundai.purchaseservice.purchase.application.port.out;

import hyundai.purchaseservice.purchase.adapter.out.dto.GetProgressPercentResponse;
import hyundai.purchaseservice.purchase.adapter.out.dto.GetScheduleDto;
import hyundai.purchaseservice.purchase.adapter.out.dto.GetScheduleResponse;
import hyundai.purchaseservice.purchase.adapter.out.dto.PartIdAndQuantityResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface PurchaseSchedulePort {
    List<PartIdAndQuantityResponse> getPartIds(List<String> partIds, LocalDate from, LocalDate to);

    Set<LocalDate> getDates(Integer year, Integer month);
    List<GetScheduleResponse> getMonthSchedule(Integer year, Integer month);

    List<GetScheduleResponse> getDaySchedule(GetScheduleDto getScheduleDto);
    Long getDayScheduleTotal(String searchType, String searchText);

    List<GetScheduleResponse> getDayScheduleWithPartIds(GetScheduleDto getScheduleDto);
    Long getDayScheduleTotalPartIds(List<String> partIds);

    List<GetScheduleResponse> getDayScheduleWithSectionIds(GetScheduleDto getScheduleDto);
    Long getDayScheduleTotalSectionIds(List<Long> sectionIds);

    List<GetProgressPercentResponse> getTomorrowSchedule();
}
