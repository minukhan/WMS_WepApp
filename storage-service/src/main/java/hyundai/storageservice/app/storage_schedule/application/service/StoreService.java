package hyundai.storageservice.app.storage_schedule.application.service;

import hyundai.storageservice.app.storage_schedule.adapter.dto.fein.SSESectionRequestDto;
import hyundai.storageservice.app.storage_schedule.application.entity.StorageSchedule;
import hyundai.storageservice.app.storage_schedule.application.port.in.StoreUseCase;
import hyundai.storageservice.app.storage_schedule.application.port.out.StorePort;
import hyundai.storageservice.app.storage_schedule.application.port.out.fein.PartFeinClient;
import hyundai.storageservice.app.storage_schedule.application.port.out.fein.SSEController;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreService implements StoreUseCase {

    private final StorePort storePort;
    private final PartFeinClient partFeinClient;
    private final SSEController sseController;

    @Override
    public void StorageStore(String partId, String sectionName) {
        String[] sectionInfo = parseSectionInput(sectionName).split(":");

        String updateSectionName  = sectionInfo[0];
        int floor  = Integer.parseInt(sectionInfo[1]);

        StorageSchedule schedual = storePort.getStorageSchedule(partId, updateSectionName, floor);

        schedual.increaseStorageQuantity();

        storePort.saveStorageSchedule(schedual);

        // part service 에서 재고 증가 업데이트
        partFeinClient.incrementInventory(partId, floor,updateSectionName);

        //SSE 생성
        SSESectionRequestDto requestDtoPlus = new SSESectionRequestDto(schedual.getToLocationSectionName(), schedual.getToLocationSectionFloor(), true);
        sseController.notifyQrScanResult(requestDtoPlus);

        // part service 에서 재고 감소 업데이트
        partFeinClient.decrementInventory( schedual.getFromLocationSectionName(), schedual.getFromLocationSectionFloor(),partId);

        //SSE 생성
        SSESectionRequestDto requestDtoMinus = new SSESectionRequestDto(schedual.getFromLocationSectionName(), schedual.getFromLocationSectionFloor(), false);
        sseController.notifyQrScanResult(requestDtoMinus);
    }

    public static String parseSectionInput(String sectionInput) {
        if (sectionInput == null || !sectionInput.matches("[A-Z]구역 \\d+열 \\d+층")) {
            throw new IllegalArgumentException("올바른 형식이 아닙니다. (예: A구역 1열 2층)");
        }

        String[] parts = sectionInput.split(" ");
        String section = parts[0].replace("구역", ""); // "A구역" -> "A"
        String row = parts[1].replace("열", ""); // "1열" -> "1"
        String floor = parts[2].replace("층", ""); // "2층" -> "2"

        return section + "-" + row + ":" + floor;  // "A-1:2" 형식으로 변환
    }
}
