package hyundai.purchaseservice.purchase.adapter.in;

import hyundai.purchaseservice.common.exception.BusinessExceptionResponse;
import hyundai.purchaseservice.purchase.adapter.in.dto.RegisterAutoRequest;
import hyundai.purchaseservice.purchase.adapter.in.dto.RegisterRequest;
import hyundai.purchaseservice.purchase.application.port.in.RegisterRequestUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name="부품 요청서 작성 API", description = "부품 요청서 작성 페이지에서 작성한 내용을 저장한다.")
public class RegisterRequestController {

    private final RegisterRequestUseCase registerRequestUseCase;

    @Operation(summary = "작성한 부품 요청서 저장", description = "관리자가 작성한 부품 요청서를 저장하고 입고 스케줄에 등록한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청서 저장 성공."),
            @ApiResponse(responseCode = "400", description = "잘못된 데이터 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BusinessExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BusinessExceptionResponse.class)))})
    @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_INFRA_MANAGER')")
    @PostMapping("/purchase/save")
    public ResponseEntity<?> registerRequest(@Parameter @RequestBody RegisterRequest request){
        registerRequestUseCase.registerRequest(request);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Operation(summary = "자동 발주 부품 요청서 저장", description = "자동 발주 서비스에서 작성한 부품 요청서를 저장하고 입고 스케줄에 등록한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청서 저장 성공."),
            @ApiResponse(responseCode = "400", description = "잘못된 데이터 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BusinessExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BusinessExceptionResponse.class)))})
    @PostMapping("/purchase/save/auto")
    public void registerAutoRequest(@RequestBody RegisterAutoRequest request){
        registerRequestUseCase.registerAutoRequest(request);
    }
}
