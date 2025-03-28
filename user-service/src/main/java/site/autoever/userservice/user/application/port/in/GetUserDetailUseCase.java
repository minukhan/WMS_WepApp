package site.autoever.userservice.user.application.port.in;

import site.autoever.userservice.user.adapter.in.dto.UserDetailResponse;

public interface GetUserDetailUseCase {
    UserDetailResponse getUserDetail(long userId);
}
