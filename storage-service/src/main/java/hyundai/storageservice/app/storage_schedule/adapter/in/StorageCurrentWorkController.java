package hyundai.storageservice.app.storage_schedule.adapter.in;

import hyundai.storageservice.app.storage_schedule.adapter.dto.DayScheduleResponse;
import hyundai.storageservice.app.storage_schedule.application.port.in.StorageCurrentWorkUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "적치 일정 API", description = "창고 적치 일정 관련 API")
public class StorageCurrentWorkController  {

    private final StorageCurrentWorkUseCase storageCurrentWorkUseCase;

    @GetMapping("/storage/day/work/detail")
    @Operation(summary = "일일 적치 일정 조회", description = "오늘 날짜에 해당하는 창고 적치 일정을 조회합니다.")
    public DayScheduleResponse getDaySchedule() {
        return storageCurrentWorkUseCase.getDaySchedule();
    }
}
