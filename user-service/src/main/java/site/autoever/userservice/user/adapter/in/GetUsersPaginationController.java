package site.autoever.userservice.user.adapter.in;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.autoever.userservice.user.application.dto.UserInfoDto;
import site.autoever.userservice.user.application.port.in.GetUsersPaginationUseCase;

@RestController
@RequiredArgsConstructor
@Tag(name = "유저 정보 페이지네이션 조회", description = "사용자 정보 조회 API")
public class GetUsersPaginationController {

    private final GetUsersPaginationUseCase getUsersPaginationUseCase;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_INFRA_MANAGER', 'ROLE_WAREHOUSE_MANAGER')")
    @Operation(summary = "전체 사용자 정보 조회", description = "페이지네이션을 통해 전체 사용자 정보를 조회합니다.")
    @GetMapping("/users")
    public Page<UserInfoDto> getAllLogs(
            @Parameter(description = "페이지 번호", example = "0")
            @RequestParam(required = false, defaultValue = "0") int page,
            @Parameter(description = "페이지 크기", example = "20")
            @RequestParam(required = false, defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return getUsersPaginationUseCase.getAllUsers(pageable);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_INFRA_MANAGER', 'ROLE_WAREHOUSE_MANAGER')")
    @Operation(summary = "필터 조건으로 사용자 정보 조회", description = "이름, 역할, 전화번호 등의 필터 조건을 통해 사용자 정보를 페이지네이션하여 조회합니다.")
    @GetMapping("/users/filter")
    public Page<UserInfoDto> getUsersByFilter(
            @Parameter(description = "검색하려는 타입")
            @RequestParam(required = false) String searchType,
            @Parameter(description = "검색하려는 값")
            @RequestParam(required = false) String searchText,
            @Parameter(description = "페이지 번호", example = "0")
            @RequestParam(required = false, defaultValue = "0") int page,
            @Parameter(description = "페이지 크기", example = "20")
            @RequestParam(required = false, defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        if (searchType != null && searchText != null) {
            return switch (searchType) {
                case "name" -> getUsersPaginationUseCase.getUsersByName(searchText, pageable);
                case "role" -> getUsersPaginationUseCase.getUsersByRole(searchText, pageable);
                case "phoneNumber" -> getUsersPaginationUseCase.getUserByPhoneNumber(searchText, pageable);
                case "email" -> getUsersPaginationUseCase.getUserByEmail(searchText, pageable);
                default -> getUsersPaginationUseCase.getAllUsers(pageable);
            };
        }

        return getUsersPaginationUseCase.getAllUsers(pageable);
    }
}
