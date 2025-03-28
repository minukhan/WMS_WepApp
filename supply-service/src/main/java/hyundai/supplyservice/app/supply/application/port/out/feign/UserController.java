package hyundai.supplyservice.app.supply.application.port.out.feign;

import com.fasterxml.jackson.databind.JsonNode;
import hyundai.supplyservice.app.supply.application.port.in.commondto.UserInfoDto;
import hyundai.supplyservice.app.supply.application.port.in.commondto.UserInfoRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "user-service")
public interface UserController {
    @PostMapping("/users/batch")
    List<UserInfoDto> getUsersByIds(@RequestBody UserInfoRequestDto ids);


    // 목록 조회시 주문자 이름 표시
    @PostMapping("/users/batch")
    JsonNode getUserNameById(@RequestBody UserInfoRequestDto userIds);

    // 목록 조회에서 유저 이름으로 검색
    @GetMapping("users/search")
    JsonNode findUserIdsByName(@RequestParam String name);

}


