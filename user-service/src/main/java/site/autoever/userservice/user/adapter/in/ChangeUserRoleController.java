package site.autoever.userservice.user.adapter.in;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import site.autoever.userservice.user.adapter.in.dto.ChangeUserRoleRequest;
import site.autoever.userservice.user.adapter.in.dto.UserDetailResponse;
import site.autoever.userservice.user.application.port.in.ChangeUserRoleUseCase;

@RestController
@RequiredArgsConstructor
@Tag(name = "유저 역할 변경 API [ADMIN만 가능]", description = "사용자 정보 변경 API")
public class ChangeUserRoleController {

    private final ChangeUserRoleUseCase changeUserRoleUseCase;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/users/role")
    public ResponseEntity<UserDetailResponse> changeUserRole(@RequestBody ChangeUserRoleRequest request) {
        return ResponseEntity.ok(changeUserRoleUseCase.modifyUser(request));
    }

}
