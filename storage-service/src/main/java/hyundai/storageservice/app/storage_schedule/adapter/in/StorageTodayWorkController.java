package hyundai.storageservice.app.storage_schedule.adapter.in;


import hyundai.storageservice.app.storage_schedule.adapter.dto.ProgressChartResponse;
import hyundai.storageservice.app.storage_schedule.application.port.in.StorageTodayWorkUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "적치 API", description = "창고 적치 일정 관련 API")
public class StorageTodayWorkController {

    private final StorageTodayWorkUseCase storageTodayWorkUseCase;

    @Operation(summary = "오늘의 창고 작업 현황 조회", description = "오늘의 창고 작업 진행 상황을 반환합니다.")
    @GetMapping("/storage/today/work")
    public ProgressChartResponse getTodayWork() {
        return storageTodayWorkUseCase.getProgressChart();
    }
}
