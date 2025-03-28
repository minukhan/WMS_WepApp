package site.autoever.userservice.user.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import site.autoever.userservice.infrastructure.util.RedisUtil;
import site.autoever.userservice.user.adapter.in.dto.CreateTempUserRequest;
import site.autoever.userservice.user.adapter.in.dto.CreateTempUserResponse;
import site.autoever.userservice.user.application.exception.TempUserNotFoundException;
import site.autoever.userservice.user.application.port.in.CreateTempUserUseCase;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateTempUserService implements CreateTempUserUseCase {

    private static final String ROLE_WORKER = "ROLE_WAREHOUSE_WORKER";

    private final RedisUtil redisUtil;

    public CreateTempUserResponse createTempUser(CreateTempUserRequest request) {
        // Redis에서 해당 tempUserId에 대한 managerId 조회
        Long existingManagerId = Long.valueOf(redisUtil.getData(request.tempUserId(), Integer.class));

        if (existingManagerId != null) {
            // 기존 managerId가 존재하면, 검증 후 리턴
            log.info("Auth 서비스에서 발행한 정보와 일치합니다. ID: {} (Manager ID: {})", request.tempUserId(), existingManagerId);
            return new CreateTempUserResponse(request.tempUserId(), existingManagerId, ROLE_WORKER);
        }

        log.error("사용자 정보가 auth에서 발급한 redis와 일치하지 않거나 존재하지 않습니다.");
        throw new TempUserNotFoundException();
    }
}
