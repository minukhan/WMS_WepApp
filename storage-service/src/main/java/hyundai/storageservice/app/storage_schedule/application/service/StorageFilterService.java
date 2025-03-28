package hyundai.storageservice.app.storage_schedule.application.service;

import hyundai.storageservice.app.storage_schedule.adapter.dto.DayTodoDto;
import hyundai.storageservice.app.storage_schedule.adapter.dto.FilterResponse;
import hyundai.storageservice.app.storage_schedule.adapter.dto.fein.PartPurchaseResponse;
import hyundai.storageservice.app.storage_schedule.application.entity.StorageSchedule;
import hyundai.storageservice.app.storage_schedule.application.port.in.StorageFilterUseCase;
import hyundai.storageservice.app.storage_schedule.application.port.out.StorageFilterPort;
import hyundai.storageservice.app.storage_schedule.application.port.out.fein.PartFeinClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StorageFilterService implements StorageFilterUseCase {

    private final StorageFilterPort storageFilterPort;
    private final PartFeinClient partFeinClient;

    @Override
        public FilterResponse getFilterResponse(String searchType, String searchText, String orderType, boolean isDesc) {

        List<StorageSchedule> storageSchedules = storageFilterPort.findAll();

        // 검색 조건이 제공되면 필터링 적용 (검색 조건은 하나씩만 사용)
        if (searchType != null && !searchType.isBlank() &&
                searchText != null && !searchText.isBlank()) {

            String lowerSearchText = searchText.toLowerCase();
            switch (searchType) {
                case "품목코드":
                    storageSchedules = storageSchedules.stream()
                            .filter(s -> s.getPartId().toLowerCase().contains(lowerSearchText))
                            .collect(Collectors.toList());
                    break;
                case "품목명":
                    storageSchedules = storageSchedules.stream()
                            .filter(s -> {
                                PartPurchaseResponse part = partFeinClient.findPart(s.getPartId());
                                return part.partSupplierDto().partName().toLowerCase().contains(lowerSearchText);
                            })
                            .collect(Collectors.toList());
                    break;
                case "현재구역":
                    storageSchedules = storageSchedules.stream()
                            .filter(s -> s.getFromLocationSectionName().toLowerCase().contains(lowerSearchText))
                            .collect(Collectors.toList());
                    break;
                case "적치구역":
                    storageSchedules = storageSchedules.stream()
                            .filter(s -> s.getToLocationSectionName().toLowerCase().contains(lowerSearchText))
                            .collect(Collectors.toList());
                    break;
                default:
                    // 잘못된 검색 타입이면 필터링 없이 진행
                    break;
            }
        }

        Comparator<StorageSchedule> comparator;
        if (orderType != null && !orderType.isBlank()) {
            switch (orderType) {
                case "수량":
                    comparator = Comparator.comparing(StorageSchedule::getPartQuantity);
                    break;
                case "금액":
                    comparator = Comparator.comparing(schedule -> {
                        PartPurchaseResponse part = partFeinClient.findPart(schedule.getPartId());
                        int total = schedule.getPartQuantity() * part.partSupplierDto().price().intValue();
                        return total;
                    });
                    break;
                default:
                    comparator = Comparator.comparing(StorageSchedule::getId);
                    break;
            }
        } else {
            comparator = Comparator.comparing(StorageSchedule::getId);
        }
        // 정렬 순서 적용: isDesc가 true이면 내림차순, 아니면 오름차순
        if (isDesc) {
            comparator = comparator.reversed();
        }

        storageSchedules.sort(comparator);

        List<DayTodoDto> dayTodoDtos = storageSchedules.stream()
                .map(storageSchedule ->
                        {
                            PartPurchaseResponse part = partFeinClient.findPart(storageSchedule.getPartId()); // part 정보 가져오기
                            int total = (int) (storageSchedule.getPartQuantity() * part.partSupplierDto().price());

                            return DayTodoDto.of(
                                    storageSchedule.getPartId(),
                                    part.partSupplierDto().partName(),
                                    storageSchedule.getPartQuantity(),
                                    total,
                                    changeSection(storageSchedule.getFromLocationSectionName(), storageSchedule.getFromLocationSectionFloor()),
                                    changeSection(storageSchedule.getToLocationSectionName(), storageSchedule.getToLocationSectionFloor())
                            );
                        }).collect(Collectors.toList());

        // 결과를 DTO로 변환하여 반환 (FilterResponse.from() 메서드는 리스트를 받아 DTO로 변환하는 정적 팩토리 메서드로 가정)
        return FilterResponse.from(dayTodoDtos);
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
