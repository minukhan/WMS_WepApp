package site.autoever.userservice.user.application.port.in;

import site.autoever.userservice.user.application.dto.ClientUserInfoDto;

public interface ClientUserInfoUseCase {
    ClientUserInfoDto getClientInfo();
}
