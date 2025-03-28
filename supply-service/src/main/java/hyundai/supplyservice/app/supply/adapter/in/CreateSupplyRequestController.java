package hyundai.supplyservice.app.supply.adapter.in;

import hyundai.supplyservice.app.supply.application.port.in.create.CreateSupplyRequestDto;
import hyundai.supplyservice.app.supply.application.port.in.create.CreateSupplyRequestResponseDto;
import hyundai.supplyservice.app.supply.application.port.in.create.CreateSupplyRequestUsecase;
import hyundai.supplyservice.app.supply.application.port.in.commondto.PartInfoDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasAnyAuthority('ROLE_VERIFIED_USER','ROLE_ADMIN')")
@RequestMapping("/supply/requests")
@Tag(name = "공급 요청 생성")
public class CreateSupplyRequestController {

    private final CreateSupplyRequestUsecase createSupplyRequestUsecase;


    @PostMapping("/create")
    @Operation(summary = "공급 요청 생성/주문서 제출하기", description = "현대공장에서 부품 주문서를 작성한다.")
    public ResponseEntity<CreateSupplyRequestResponseDto> createSupplyRequest(
            @RequestBody CreateSupplyRequestDto command
    ) {

        CreateSupplyRequestResponseDto createdResponse = createSupplyRequestUsecase.createSupplyRequest(command);
        return ResponseEntity.ok(createdResponse);
    }

    @GetMapping("/parts")
    @Operation(summary = "부품검색을 위한 부품 전체 조회")
    public ResponseEntity<List<PartInfoDto>> getAllParts() {
        List<PartInfoDto> parts = createSupplyRequestUsecase.getAllPartsInfo();
        return ResponseEntity.ok(parts);

    }



}
