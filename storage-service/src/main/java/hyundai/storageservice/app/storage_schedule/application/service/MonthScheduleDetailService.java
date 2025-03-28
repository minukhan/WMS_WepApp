package hyundai.storageservice.app.storage_schedule.application.service;

import hyundai.storageservice.app.storage_schedule.adapter.dto.DayTodoDto;
import hyundai.storageservice.app.storage_schedule.adapter.dto.MonthScheduleDayDto;
import hyundai.storageservice.app.storage_schedule.adapter.dto.MonthScheduleResponse;
import hyundai.storageservice.app.storage_schedule.adapter.dto.fein.PartPurchaseResponse;
import hyundai.storageservice.app.storage_schedule.application.entity.StorageSchedule;
import hyundai.storageservice.app.storage_schedule.application.port.in.MonthScheduleDetailUseCase;
import hyundai.storageservice.app.storage_schedule.application.port.out.MonthScheduleDetailPort;
import hyundai.storageservice.app.storage_schedule.application.port.out.fein.PartFeinClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MonthScheduleDetailService implements MonthScheduleDetailUseCase {

    private final MonthScheduleDetailPort monthScheduleDetailPort;
    private final PartFeinClient partFeinClient;

    @Override
    public MonthScheduleResponse getMonthSchedule(String year, String month) {

        List<StorageSchedule> storageSchedules = monthScheduleDetailPort.getStorageSchedule();

        // 입력된 연도와 월을 기반으로 LocalDate 필터링
        int y = Integer.parseInt(year);
        int m = Integer.parseInt(month);

        List<StorageSchedule> filteredList = storageSchedules.stream()
                .filter(schedual -> schedual.getScheduledAt().getYear() == y) // 연도 필터링
                .filter(schedual -> schedual.getScheduledAt().getMonthValue() == m) // 월 필터링
                .toList(); // 필터링된 리스트 생성

            // 날짜별 그룹화
            Map<LocalDate, List<DayTodoDto>> groupedByDate = filteredList.stream()
                    .collect(Collectors.groupingBy(
                            StorageSchedule::getScheduledAt, // 날짜별 그룹화
                            Collectors.mapping(filter -> {
                                PartPurchaseResponse part = partFeinClient.findPart(filter.getPartId()); // part 정보 가져오기
                                int total = (int) (filter.getPartQuantity() * part.partSupplierDto().price());

                                return DayTodoDto.of(
                                        filter.getPartId(),
                                        part.partSupplierDto().partName(),
                                        filter.getPartQuantity(),
                                        total,
                                        changeSection(filter.getFromLocationSectionName(), filter.getFromLocationSectionFloor()),
                                        changeSection(filter.getToLocationSectionName(), filter.getToLocationSectionFloor())
                                );
                            }, Collectors.toList()) // DayTodoDto 리스트로 변환
                    ));

            // 날짜별 DayTodoDto 리스트를 MonthScheduleDayDto 리스트로 변환
            List<MonthScheduleDayDto> monthScheduleDayDtos = groupedByDate.entrySet().stream()
                    .map(entry -> MonthScheduleDayDto.of(entry.getKey(), entry.getValue()))
                    .toList();


        return MonthScheduleResponse.from(monthScheduleDayDtos);
    }


    private String changeSection(String sectionName, int floor) {
        if (sectionName == null || !sectionName.contains("-")) {
            throw new IllegalArgumentException("올바른 형식의 sectionName이 아닙니다: " + sectionName);
        }

        // "A-4" → ["A", "4"] 분리
        String[] parts = sectionName.split("-");

        // 결과 문자열 생성
        return String.format("%s구역 %s열 %d층", parts[0], parts[1], floor);
    }
}
