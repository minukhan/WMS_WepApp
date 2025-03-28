package site.autoever.userservice.user.adapter.in;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserSwaggerController {
    @GetMapping("/actuator/info")
    public String info() {
        return "redirect:/swagger-ui/index.html";
    }
}
