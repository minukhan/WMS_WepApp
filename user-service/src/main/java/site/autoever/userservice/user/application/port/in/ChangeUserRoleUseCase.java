package site.autoever.userservice.user.application.port.in;

import site.autoever.userservice.user.adapter.in.dto.ChangeUserRoleRequest;
import site.autoever.userservice.user.adapter.in.dto.UserDetailResponse;

public interface ChangeUserRoleUseCase {
    UserDetailResponse modifyUser(ChangeUserRoleRequest request);
}
