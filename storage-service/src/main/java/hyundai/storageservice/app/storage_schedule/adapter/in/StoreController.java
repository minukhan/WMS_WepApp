package hyundai.storageservice.app.storage_schedule.adapter.in;

import hyundai.storageservice.app.storage_schedule.application.port.in.StoreUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequiredArgsConstructor
@Tag(name = "창고 적치 API", description = "창고 적치 및 입고 관련 API")
public class StoreController {

    private final StoreUseCase storeUseCase;

    @PostMapping("/storage/store")
    @Operation(summary = "입고 처리", description = " QR로 지정된 구역에 특정 품목을 입고 처리합니다.")
    public ResponseEntity<String> getStore(
            @Parameter(description = "품목 코드", example = "BHP100") @RequestParam String partId,
            @Parameter(description = "입고 구역", example = "A구역 1열 2층") @RequestParam String sectionName
    ) {
        storeUseCase.StorageStore(partId, sectionName);
        return ResponseEntity.ok("성공적으로 업데이트 되었습니다.");
    }
}
