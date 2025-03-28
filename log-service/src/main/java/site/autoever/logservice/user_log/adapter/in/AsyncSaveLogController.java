package site.autoever.logservice.user_log.adapter.in;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import site.autoever.logservice.user_log.adapter.in.dto.LogCreateRequest;
import site.autoever.logservice.user_log.application.port.in.AsyncSaveLogUseCase;
import site.autoever.logservice.user_log.application.port.in.SaveLogUseCase;

@RestController
@RequiredArgsConstructor
@Tag(name = "로그 생성", description = "로그 생성 API")
public class AsyncSaveLogController {

    private final AsyncSaveLogUseCase asyncSaveLogUseCase;

    @Operation(summary = "로그 생성", description = "사용자의 로그를 생성합니다.")
    @PostMapping("/logs/save")
    public ResponseEntity<Void> saveLog(@RequestBody LogCreateRequest request) {
        asyncSaveLogUseCase.saveLog(request.toEntity());
        return ResponseEntity.ok().build();
    }
}
