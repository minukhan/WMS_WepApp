package site.autoever.logservice.user_log.application.port.out;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import site.autoever.logservice.user_log.application.dto.UserIdsDto;
import site.autoever.logservice.user_log.application.dto.UserInfoDto;

import java.util.List;

@FeignClient(name = "user-service", path = "/users")
public interface GetUsersPort {
    @PostMapping("/batch")
    List<UserInfoDto> getUsersByIds(@RequestBody UserIdsDto userIdsDto);

    @GetMapping("/search")
    List<UserInfoDto> getUsersByEmail(@RequestParam("email") String email);

    @GetMapping("/search")
    List<UserInfoDto> getUsersByName(@RequestParam("name") String name);

    @GetMapping("/search")
    List<UserInfoDto> getUsersByPhoneNumber(@RequestParam("phone-number") String phoneNumber);

    @GetMapping("/search")
    List<UserInfoDto> getUsersByRole(@RequestParam("role") String role);
}
