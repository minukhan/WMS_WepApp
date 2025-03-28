package site.autoever.logservice.user_log.adapter.in;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LogSwaggerController {
    @GetMapping("/actuator/info")
    public String info() {
        return "redirect:/swagger-ui/index.html";
    }
}
