package hyundai.storageservice.app.storage_schedule.application.service;

import hyundai.storageservice.app.storage_schedule.adapter.dto.DayScheduleDetailDto;
import hyundai.storageservice.app.storage_schedule.adapter.dto.DayTodoMoveDto;
import hyundai.storageservice.app.storage_schedule.adapter.dto.WorkerDayScheduleResponse;
import hyundai.storageservice.app.storage_schedule.adapter.dto.fein.PartPurchaseResponse;
import hyundai.storageservice.app.storage_schedule.application.entity.StorageSchedule;
import hyundai.storageservice.app.storage_schedule.application.port.in.StorageTodayWorkWithDetailUseCase;
import hyundai.storageservice.app.storage_schedule.application.port.out.StorageCurrentWorkPort;
import hyundai.storageservice.app.storage_schedule.application.port.out.fein.PartFeinClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StorageTodayWorkWithDetailService implements StorageTodayWorkWithDetailUseCase {

    private final StorageCurrentWorkPort storageCurrentWorkPort;
    private final PartFeinClient partFeinClient;

    @Override
    public WorkerDayScheduleResponse getWorkerDaySchedule() {
        LocalDate date = LocalDate.now();
        List<StorageSchedule> storageSchedules = storageCurrentWorkPort.getCurrentWork(date);

        List<DayTodoMoveDto> dayScheduleDetails = storageSchedules.stream()
                .map(storageSchedual -> {
                    String sectionName = parseSectionName(storageSchedual.getToLocationSectionName());
                    String sectionFloor = parseSectionFloor(storageSchedual.getToLocationSectionName(), storageSchedual.getToLocationSectionFloor());
                    String preSectionName = parseSectionName(storageSchedual.getFromLocationSectionName());
                    String preSectionFloor = parseSectionFloor(storageSchedual.getFromLocationSectionName(), storageSchedual.getFromLocationSectionFloor());
                    PartPurchaseResponse part = partFeinClient.findPart(storageSchedual.getPartId());

                    return new DayTodoMoveDto(
                            part.partSupplierDto().partName(),
                            storageSchedual.getPartQuantity(),
                            storageSchedual.getStorageQuantity(),
                            sectionName,
                            sectionFloor,
                            preSectionName,
                            preSectionFloor
                    );
                })
                .collect(Collectors.toList());

        return WorkerDayScheduleResponse.of(date, dayScheduleDetails);
    }

    //  입고 구역 변환 (예: "A-3" → "A구역")
    private String parseSectionName(String partName) {
        if (partName == null || !partName.contains("-")) {
            return partName; // 변환 불가능한 경우 원본 유지
        }
        return partName.split("-")[0] + "구역";  // "A-3" → "A구역"
    }

    //  입고 위치 변환 (예: "A-3", 5 → "3열 5층")
    private String parseSectionFloor(String partName, Integer floor) {
        if (partName == null || !partName.contains("-")) {
            return (floor != null) ? floor + "층" : "알 수 없음";
        }
        String[] parts = partName.split("-");
        String row = parts.length > 1 ? parts[1] + "열" : "알 수 없음"; // "A-3" → "3열"
        String floorText = (floor != null) ? floor + "층" : "알 수 없음";
        return row + " " + floorText;  // "3열 5층"
    }
}
