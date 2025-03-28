package site.autoever.userservice.user.application.port.in;

import site.autoever.userservice.user.application.dto.AdminUserInfoDto;

public interface AdminUserInfoUseCase {
    AdminUserInfoDto getAdminInfo();
}
