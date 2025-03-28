package site.autoever.userservice.user.adapter.in;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import site.autoever.userservice.user.adapter.in.dto.UserDetailResponse;
import site.autoever.userservice.user.application.port.in.GetUserDetailUseCase;

@RestController
@RequiredArgsConstructor
@Tag(name = "유저 상세 정보 조회", description = "사용자 아이디에 해당하는 상세 정보 조회 API")
public class GetUserDetailController {
    private final GetUserDetailUseCase getUserDetailUseCase;

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserDetailResponse> getUserDetail(
            @PathVariable(value = "userId") long userId
    ) {
        return ResponseEntity.ok(getUserDetailUseCase.getUserDetail(userId));
    }
}
