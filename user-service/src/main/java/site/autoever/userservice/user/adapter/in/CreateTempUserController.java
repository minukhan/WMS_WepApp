package site.autoever.userservice.user.adapter.in;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import site.autoever.userservice.user.adapter.in.dto.CreateTempUserRequest;
import site.autoever.userservice.user.adapter.in.dto.CreateTempUserResponse;
import site.autoever.userservice.user.application.port.in.CreateTempUserUseCase;

@RestController
@RequiredArgsConstructor
@Tag(name = "임시 사용자를 생성 API", description = "임시 사용자 id를 받아서 redis에 저장합니다.")
public class CreateTempUserController {

    private final CreateTempUserUseCase createTempUserUseCase;

    @Operation(summary = "임시 사용자 생성", description = "임시 사용자를 생성합니다.")
    @PostMapping("/users/temp-user")
    public ResponseEntity<CreateTempUserResponse> createTempUser(@Valid @RequestBody CreateTempUserRequest request) {
        return ResponseEntity.ok(createTempUserUseCase.createTempUser(request));
    }
}
