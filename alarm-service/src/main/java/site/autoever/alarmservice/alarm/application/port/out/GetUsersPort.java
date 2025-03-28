package site.autoever.alarmservice.alarm.application.port.out;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import site.autoever.alarmservice.alarm.application.dto.UserInfoDto;

import java.util.List;

@FeignClient(name = "user-service", path = "/users")

public interface GetUsersPort {
    @GetMapping("/search")
    List<UserInfoDto> getUsersByRole(@RequestParam("role") String role);
}
