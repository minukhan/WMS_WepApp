package hyundai.storageservice.app.storage_schedule.adapter.in;


import hyundai.storageservice.app.storage_schedule.application.port.in.UploadStorageScheduleUseCase;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequiredArgsConstructor
@Tag(name = "적치 API", description = "창고 적치 일정 관련 API")
public class UploadStorageScheduleController {

    private final UploadStorageScheduleUseCase uploadStorageScheduleUseCase;

    @Operation(summary = "창고 적치 일정 업로드", description = "창고의 적치 일정을 업로드합니다.")
    @PostMapping("/storage/upload")
    public ResponseEntity<String> uploadStorage() {
        uploadStorageScheduleUseCase.uploadStorageScheduleUseCase();
        return ResponseEntity.ok("성공적으로 적치 일정이 업데이트 되었습니다.");
    }
}
