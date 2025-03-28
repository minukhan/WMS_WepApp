package hyundai.storageservice.app.storage_schedule.adapter.in;

import hyundai.storageservice.app.storage_schedule.adapter.dto.WorkerDayScheduleResponse;
import hyundai.storageservice.app.storage_schedule.application.port.in.StorageTodayWorkWithDetailUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "적치 일정 API", description = "창고 적치 일정 및 작업 상세 조회 API")
public class StorageTodayWorkWithDetailController {

    private final StorageTodayWorkWithDetailUseCase storageTodayWorkWithDetailUseCase;

    @GetMapping("/storage/today/work/detail/section")
    @Operation(summary = "작업 상세 조회", description = "오늘 창고 적치 일정의 섹션별 작업 상세 정보를 조회합니다.")
    public WorkerDayScheduleResponse getTodayWorkDetail() {
        return storageTodayWorkWithDetailUseCase.getWorkerDaySchedule();
    }
}
