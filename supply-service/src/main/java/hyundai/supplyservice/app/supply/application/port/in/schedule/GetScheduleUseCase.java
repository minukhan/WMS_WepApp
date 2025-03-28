package hyundai.supplyservice.app.supply.application.port.in.schedule;

import java.time.LocalDate;
import java.util.List;

public interface GetScheduleUseCase {

    // 월별 출고 일정 조회, 달력
    CalendarResponseDto getCalendar(Integer year, Integer month);

    // 월별 출고 일정 전체 조회
    MonthlySupplyScheduleResponse getMonthlySupplySchedule(Integer year,Integer month);

    // 날짜별 출고 품목 및 현재 진행 개수
    DailySupplyScheduleResponse getDailySupplyPartSchedule();

    // 날짜별 출고 진척도 % - 모든 부품 합산
    DailySupplyProgressResponseDto getDailyProgress();

    // 날짜별 출고 품목 및 금액 목록 (정렬 및 검색 가능)
    List<DailySupplyPartPriceResponseDto> getDailyParts(String searchType, String searchText, String orderType, Boolean isDesc);

    // 현장 근무자 오늘의 일정 - 부품별 출고 위치 및 현황
    DailySectionScheduleResponseDto getDailySectionSchedule();

    // 현장 근무자 부품 qr찍어서 출고
    void processQrScanOutbound(String partId, String sectionName);
}
