package hyundai.storageservice.app.storage_schedule.application.service;


import hyundai.storageservice.app.storage_schedule.adapter.dto.CalendarDateResponse;
import hyundai.storageservice.app.storage_schedule.application.entity.StorageSchedule;
import hyundai.storageservice.app.storage_schedule.application.port.in.MonthScheduleUseCase;
import hyundai.storageservice.app.storage_schedule.application.port.out.MonthSchedulePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MonthScheduleService implements MonthScheduleUseCase {

    private final MonthSchedulePort monthSchedulePort;

    @Override
    public CalendarDateResponse getCalendarDate(String year, String month) {
        // 모든 일정 가져오기
        List<StorageSchedule> storageSchedules = monthSchedulePort.getStorageSchedule();

        // 입력된 연도와 월을 기반으로 LocalDate 필터링
        int y = Integer.parseInt(year);
        int m = Integer.parseInt(month);

        List<LocalDate> filteredDates = storageSchedules.stream()
                .map(StorageSchedule::getScheduledAt) // 적치 일정 날짜 가져오기
                .filter(date -> date.getYear() == y && date.getMonthValue() == m) // 해당 연도, 월에 해당하는 날짜 필터링
                .distinct() // 중복 제거
                .toList();

        return new CalendarDateResponse(filteredDates);
    }

}
