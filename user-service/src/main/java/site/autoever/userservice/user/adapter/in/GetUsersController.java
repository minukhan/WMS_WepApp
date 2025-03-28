package site.autoever.userservice.user.adapter.in;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.autoever.userservice.user.adapter.in.dto.UserIdsRequest;
import site.autoever.userservice.user.application.dto.UserInfoDto;
import site.autoever.userservice.user.application.port.in.GetUsersUseCase;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "유저 정보 조회 [Feign Client]", description = "사용자 정보 조회 API")
public class GetUsersController {

    private final GetUsersUseCase getUsersUseCase;

    @Operation(summary = "사용자 ID 리스트로 사용자 정보 조회", description = "주어진 사용자 ID 리스트를 통해 사용자 정보를 조회합니다.")
    @PostMapping("/users/batch")
    public List<UserInfoDto> getUsersByIds(
            @Parameter(description = "사용자 ID 리스트 요청 객체", required = true)
            @RequestBody UserIdsRequest request) {
        return getUsersUseCase.getUsersByIds(request.userIds());
    }

    @Operation(summary = "사용자 검색", description = "이메일, 이름, 전화번호 또는 역할을 기준으로 사용자 정보를 검색합니다.")
    @GetMapping("/users/search")
    public List<UserInfoDto> getUserInfo(
            @Parameter(description = "사용자 이메일")
            @RequestParam(required = false) String email,
            @Parameter(description = "사용자 이름")
            @RequestParam(required = false) String name,
            @Parameter(description = "사용자 전화번호")
            @RequestParam(required = false, value = "phone-number") String phoneNumber,
            @Parameter(description = "사용자 역할")
            @RequestParam(required = false) String role) {

        if (email != null) {
            return getUsersUseCase.getUsersByEmail(email);
        } else if (name != null) {
            return getUsersUseCase.getUsersByName(name);
        } else if (phoneNumber != null) {
            return getUsersUseCase.getUserByPhoneNumber(phoneNumber);
        } else if (role != null) {
            return getUsersUseCase.getUsersByRole(role);
        }

        return List.of();
    }
}
