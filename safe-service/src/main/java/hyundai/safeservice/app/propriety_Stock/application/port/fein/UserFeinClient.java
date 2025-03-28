package hyundai.safeservice.app.propriety_Stock.application.port.fein;


import hyundai.safeservice.app.propriety_Stock.adapter.dto.fein.UserDetailResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface UserFeinClient {

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserDetailResponse> getUserDetail(@PathVariable(value = "userId") long userId);

}
