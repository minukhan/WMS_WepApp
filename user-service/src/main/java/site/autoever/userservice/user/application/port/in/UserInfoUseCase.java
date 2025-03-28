package site.autoever.userservice.user.application.port.in;

import site.autoever.userservice.user.application.dto.AdminUserInfoDto;
import site.autoever.userservice.user.application.dto.ClientUserInfoDto;
import site.autoever.userservice.user.application.dto.WebviewUserInfoDto;

public interface UserInfoUseCase {
    ClientUserInfoDto getClientInfo();
    AdminUserInfoDto getAdminInfo();
    WebviewUserInfoDto getWebViewInfo();
}
