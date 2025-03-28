package hyundai.supplyservice.app.supply.adapter.in;

import hyundai.supplyservice.app.supply.application.port.in.commondto.PageableResponse;
import hyundai.supplyservice.app.supply.application.port.in.read.ReadSupplyRequestUsecase;
import hyundai.supplyservice.app.supply.application.port.in.read.SupplyRequestDetailResponseDTO;
import hyundai.supplyservice.app.supply.application.port.in.read.SupplyRequestInfoDto;
import hyundai.supplyservice.app.supply.application.service.ReadSupplyRequestService;
import hyundai.supplyservice.app.supply.application.service.VerifyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "공급 요청 주문서 조회")
@PreAuthorize("hasAnyAuthority('ROLE_INFRA_MANAGER','ROLE_ADMIN','ROLE_WAREHOUSE_MANAGER')")
public class ReadSupplyRequestController {

    private final ReadSupplyRequestUsecase readSupplyRequestUsecase;
    private final VerifyService verifyService;
    private final ReadSupplyRequestService readSupplyRequestService;


    @GetMapping("/supply/requests/{requestId}")
    @Operation(summary = "공급 요청 상세 정보 조회")
    public ResponseEntity<SupplyRequestDetailResponseDTO> getRequestDetail(@PathVariable("requestId") Long requestId) {
        SupplyRequestDetailResponseDTO detailDTO = readSupplyRequestUsecase.getRequestDetail(requestId);
        return ResponseEntity.ok(detailDTO);
    }

    @GetMapping("/supply/requests")
    @Operation(summary = "주문서 목록 조회/주문자 이름 검색 및 상태별 조회 가능.")
    public ResponseEntity<PageableResponse<SupplyRequestInfoDto>> getSupplyRequestList(
            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "페이지당 항목 수", example = "10")
            @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "주문서 상태 필터 (ALL, WAITING, APPROVED, REJECTED )", example = "ALL")
            @RequestParam(defaultValue = "ALL") String status,

            @Parameter(description = "정렬 조건 (requestId, requestedAt, deadline)", example = "requestedAt")
            @RequestParam(required = false) String orderType,

            @Parameter(description = "오름차순 false, 내림차순은 true")
            @RequestParam(required = false) Boolean isDesc,

            @Parameter(description = "주문자 이름으로 검색", example = "홍길동")
            @RequestParam(required = false) String name

    ) {
        // 기본정렬 requestId 내림차순.
        Sort sort = Sort.by(Sort.Direction.DESC, "id");

        // orderType 없는데 내림차순일 경우 id 기준으로 정렬
        if (orderType == null && isDesc != null && isDesc) {
            sort = Sort.by(Sort.Direction.DESC, "id");
        }

        // orderType에 따른 정렬 조건 설정
        if (orderType != null) {
            Sort.Direction direction = (isDesc != null && isDesc) ? Sort.Direction.DESC : Sort.Direction.ASC;

            switch (orderType.toLowerCase()) {
                case "requestid":
                    sort = Sort.by(direction, "id");  //
                    break;
                case "requestedat":
                    sort = Sort.by(direction, "requestedAt");
                    break;
                case "deadline":
                    sort = Sort.by(direction, "deadline");
                    break;
                default:
                    sort = Sort.by(Sort.Direction.DESC, "id");  // 기본값도 id로 수정
            }
        }
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(readSupplyRequestUsecase.getSupplyRequestList(status, name, pageable));

    }

}