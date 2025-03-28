package hyundai.supplyservice.app.supply.adapter.in;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SupplySwaggerController {
    @GetMapping("/actuator/info")
    public String info() {
        return "redirect:/swagger-ui/index.html";
    }
}
