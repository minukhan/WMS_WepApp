package site.autoever.userservice.user.adapter.in;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import site.autoever.userservice.user.application.dto.AdminUserInfoDto;
import site.autoever.userservice.user.application.dto.ClientUserInfoDto;
import site.autoever.userservice.user.application.dto.WebviewUserInfoDto;
import site.autoever.userservice.user.application.port.in.AdminUserInfoUseCase;
import site.autoever.userservice.user.application.port.in.UserInfoUseCase;

@RestController
@RequiredArgsConstructor
@Tag(name = "어드민 유저 정보 조회", description = "로그인 정보 조회 API")
public class AdminUserInfoController {
    private final AdminUserInfoUseCase adminUserInfoUseCase;

    @Operation(summary = "로그인 한 admin 사용자 정보 조회", description = "현재 로그인 한 사용자의 정보를 조회합니다.")
    @GetMapping("/users/admin-info")
    public ResponseEntity<AdminUserInfoDto> getAdminUserInfo() {
        AdminUserInfoDto response = adminUserInfoUseCase.getAdminInfo();
        return ResponseEntity.ok(response);
    }

}
