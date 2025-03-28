package hyundai.storageservice.app.storage_schedule.application.service;

import hyundai.storageservice.app.storage_schedule.adapter.dto.DayScheduleDto;
import hyundai.storageservice.app.storage_schedule.adapter.dto.DayScheduleResponse;
import hyundai.storageservice.app.storage_schedule.adapter.dto.fein.PartPurchaseResponse;
import hyundai.storageservice.app.storage_schedule.application.entity.StorageSchedule;
import hyundai.storageservice.app.storage_schedule.application.port.in.StorageCurrentWorkUseCase;
import hyundai.storageservice.app.storage_schedule.application.port.out.StorageCurrentWorkPort;
import hyundai.storageservice.app.storage_schedule.application.port.out.fein.PartFeinClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StorageCurrentWorkService implements StorageCurrentWorkUseCase {

    private final StorageCurrentWorkPort storageCurrentWorkPort;
    private final PartFeinClient partFeinClient;
    @Override
    public DayScheduleResponse getDaySchedule() {

        LocalDate today = LocalDate.now();

        List<StorageSchedule> storageSchedules = storageCurrentWorkPort.getCurrentWork(today);

        // 부품별 수량을 합산하기 위해 Map을 사용
        Map<String, Integer> partQuantityMap = new HashMap<>();
        Map<String, Integer> storageQuantityMap = new HashMap<>();

        for (StorageSchedule schedual : storageSchedules) {
            String partId = schedual.getPartId();

            // 현재 부품의 수량을 기존 값과 더함
            partQuantityMap.put(partId, partQuantityMap.getOrDefault(partId, 0) + schedual.getPartQuantity());
            storageQuantityMap.put(partId, storageQuantityMap.getOrDefault(partId, 0) + schedual.getStorageQuantity());
        }

        // 부품 정보를 조회하고, 최종 리스트 생성
        List<DayScheduleDto> dayScheduleDtos = partQuantityMap.entrySet().stream()
                .map(entry -> {
                    String partId = entry.getKey();
                    int totalPartQuantity = entry.getValue();
                    int totalStorageQuantity = storageQuantityMap.get(partId);

                    PartPurchaseResponse part = partFeinClient.findPart(partId);

                    return DayScheduleDto.of(part.partSupplierDto().partName(), totalPartQuantity, totalStorageQuantity);
                })
                .collect(Collectors.toList());


        return DayScheduleResponse.of(today, dayScheduleDtos);
    }
}
