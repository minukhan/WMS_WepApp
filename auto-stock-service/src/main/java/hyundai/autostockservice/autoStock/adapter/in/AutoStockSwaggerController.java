package hyundai.autostockservice.autoStock.adapter.in;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AutoStockSwaggerController {

    @GetMapping("/actuator/info")
    public String info() {
        return "redirect:/swagger-ui/index.html";
    }
}
