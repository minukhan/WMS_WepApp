package site.autoever.authservice.auth.application.port.out;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import site.autoever.authservice.auth.application.dto.UserInfoDto;
import site.autoever.authservice.auth.application.dto.UserSummaryDto;
import site.autoever.authservice.auth.application.port.out.dto.TempUserInfoDto;
import site.autoever.authservice.auth.application.port.out.dto.TempUserRegisterRequest;
import site.autoever.authservice.auth.application.port.out.dto.UserDetailDto;

@FeignClient(name = "user-service", path = "/users")
public interface RegisterUserPort {
    @PostMapping("/register")
    UserSummaryDto registerUser(@RequestBody UserInfoDto userInfoDto);

    @PostMapping("/temp-user")
    TempUserInfoDto registerTempUser(@RequestBody TempUserRegisterRequest request);

    @GetMapping("/{userId}")
    UserDetailDto findUser(@PathVariable("userId") long userId);

}
