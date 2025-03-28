package site.autoever.logservice.user_log.adapter.in;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.autoever.logservice.user_log.application.dto.UserLogInfoDto;
import site.autoever.logservice.user_log.application.port.in.GetAllLogsUseCase;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@Tag(name = "모든 로그 조회 [ROLE_ADMIN 필요]", description = "로그 조회 API")
public class GetAllLogsController {

    private final GetAllLogsUseCase getAllLogsUseCase;

    @Operation(summary = "모든 로그 조회", description = "모든 로그를 최신순으로 페이징 처리하여 조회합니다.")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/logs")
    public Page<UserLogInfoDto> getAllLogs(
            @Parameter(description = "페이지 번호 (기본값: 0)") @RequestParam(required = false, defaultValue = "0") int page,
            @Parameter(description = "페이지 크기 (기본값: 20)") @RequestParam(required = false, defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return getAllLogsUseCase.getAllLogs(pageable);
    }

    @Operation(summary = "필터 조건을 사용한 로그 조회", description = "이름, 이메일, 역할, 핸드폰 번호, 메세지 조건으로 로그를 필터링하여 조회합니다.")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/logs/filter")
    public Page<UserLogInfoDto> getAllLogs(
            @Parameter(description = "검색하려는 타입 [name, email, role, phoneNumber, message] 으로 지정")
            @RequestParam(required = false) String searchType,
            @Parameter(description = "검색하려는 값 [모두 String]")
            @RequestParam(required = false) String searchText,
            @Parameter(description = "페이지 번호", example = "0")
            @RequestParam(required = false, defaultValue = "0") int page,
            @Parameter(description = "페이지 크기", example = "20")
            @RequestParam(required = false, defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        if (searchType != null && searchText != null && !searchText.isBlank()) {
            return switch (searchType) {
                case "name" -> getAllLogsUseCase.getAllLogsByName(searchText, pageable);
                case "email" -> getAllLogsUseCase.getAllLogsByEmail(searchText, pageable);
                case "role" -> getAllLogsUseCase.getAllLogsByRole(searchText, pageable);
                case "phoneNumber" -> getAllLogsUseCase.getAllLogsByPhoneNumber(searchText, pageable);
                case "message" -> getAllLogsUseCase.getAllLogsByMessage(searchText, pageable);
                default -> getAllLogsUseCase.getAllLogs(pageable);
            };
        }
        return getAllLogsUseCase.getAllLogs(pageable);
    }
}
