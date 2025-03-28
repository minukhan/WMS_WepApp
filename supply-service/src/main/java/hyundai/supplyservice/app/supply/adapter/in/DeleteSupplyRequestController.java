package hyundai.supplyservice.app.supply.adapter.in;

import hyundai.supplyservice.app.supply.application.port.in.delete.DeleteSupplyRequestUsecase;
import hyundai.supplyservice.app.supply.application.port.in.delete.DeleteSupplyResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DeleteSupplyRequestController {

    private final DeleteSupplyRequestUsecase deleteSupplyRequestUsecase;
    // 요청상세보기에서 해당 요청의삭제
    @PreAuthorize("hasAuthority('ROLE_VERIFIED_USER')")
    @DeleteMapping("/supply/requests/{ruquestId}")
    @Tag(name = "요청 삭제 API", description = "waiting 상태의 요청 삭제")
    @Operation(summary = "승인되지 않은 요청의 경우 삭제")
    public ResponseEntity<DeleteSupplyResponseDto> deleteSupplyRequest(@PathVariable("ruquestId") Long ruquestId) {
        DeleteSupplyResponseDto responseDto = deleteSupplyRequestUsecase.deleteSupplyRequest(ruquestId);
        return ResponseEntity.ok(responseDto);
    }

}
