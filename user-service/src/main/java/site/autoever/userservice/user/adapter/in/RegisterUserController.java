package site.autoever.userservice.user.adapter.in;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import site.autoever.userservice.user.adapter.in.dto.UserInfoRequest;
import site.autoever.userservice.user.application.dto.UserDetailDto;
import site.autoever.userservice.user.application.port.in.RegisterUserUseCase;

@RestController
@RequiredArgsConstructor
@Tag(name = "사용자 생성 [Feign Client]", description = "사용자 생성 엔드포인트")
public class RegisterUserController {

    private final RegisterUserUseCase registerUserUseCase;

    @Operation(
            summary = "사용자 정보 dto로 사용자 정보 생성",
            description = "클라이언트에서 전달한 사용자 정보 dto를 통해 사용자를 생성한다."
    )

    @PostMapping("/users/register")
    public UserDetailDto register(
            @Parameter(description = "사용자 정보 request 객체", required = true)
            @RequestBody UserInfoRequest request) {
        return registerUserUseCase.registerUser(request.toEntity());
    }
}
