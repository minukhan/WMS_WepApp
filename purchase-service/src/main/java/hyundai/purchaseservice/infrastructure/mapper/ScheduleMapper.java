package hyundai.purchaseservice.infrastructure.mapper;

import hyundai.purchaseservice.purchase.adapter.out.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Mapper
public interface ScheduleMapper {
    // 입고 스케줄 저장
    void registerSchedule(List<SaveScheduleDto> saveScheduleDto);

    // 입고 예정 물품과 수량
    List<PartIdAndQuantityResponse> getPartIdAndQuantity(List<String> partIds, LocalDate from, LocalDate to);

    // 입고 예정날까지 각 section에 입고될 품목 수량
    List<StoreDeliveryAmountBySectionIdDto> storeAmountUntilArrivalDate(List<Long> sectionIds, LocalDate to);

    // 입고 예정날까지 section에 입고될 전체 수량
    List<StoreDeliveryAmountBySectionIdDto> storeAmountUntilArrivalDateBySection(LocalDate to);

    // 특정 날까지 입고 수량
    Long getTotalStoreAmountUntilArrivalDate(LocalDate to);

    /************************* 스케줄 조회 *************************/
    // month 내 입고 일정이 있는 날짜
    Set<LocalDate> getScheduleDates(Integer year, Integer month);

    // month 내 입고 일정 내용
    List<GetScheduleResponse> getSchedules(Integer year, Integer month);

    // 오늘 입고 물품 수량 - 품목 코드별 분류
    List<GetDayScheduleQuantitesResponse> getDaySchedule();
    // 내일 입고 물품 수량, 위치 조회
    List<GetProgressPercentResponse> getTomorrowSchedule();

    // 입고 일정 조회(검색, 정렬 가능)
    List<GetScheduleResponse> getDayScheduleSearch(GetScheduleDto getScheduleDto);
    Long getDayScheduleSearchCount(String searchType, String searchText);
    List<GetScheduleResponse> getDayScheduleWithPartIds(GetScheduleDto getScheduleDto);
    Long getDayScheduleCountWithPartIds(List<String> partIds);
    List<GetScheduleResponse> getDayScheduleWithSectionIds(GetScheduleDto getScheduleDto);
    Long getDayScheduleCountWithSectionIds(List<Long> sectionIds);

    /************************* 통계 *************************/
    List<GetProgressPercentResponse> getProgressPercent();

    /************************* 입고 QR *************************/
    List<ScheduleIdAndQuantityResponse> checkProcessedQuantity(String partId, Long sectionId);

    void checkWorkingRequest(Long requestId);

    void addProcessedQuantity(Long scheduleId, Integer processedQuantity, Long requestId);
}
