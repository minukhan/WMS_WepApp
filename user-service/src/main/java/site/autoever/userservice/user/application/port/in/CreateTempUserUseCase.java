package site.autoever.userservice.user.application.port.in;

import site.autoever.userservice.user.adapter.in.dto.CreateTempUserRequest;
import site.autoever.userservice.user.adapter.in.dto.CreateTempUserResponse;

public interface CreateTempUserUseCase {
    CreateTempUserResponse createTempUser(CreateTempUserRequest request);
}
