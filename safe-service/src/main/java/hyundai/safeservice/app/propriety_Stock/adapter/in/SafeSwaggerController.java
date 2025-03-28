package hyundai.safeservice.app.propriety_Stock.adapter.in;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Tag(name = "스웨거 주소 설정", description = "스웨거 주소 redirect 해주는 컨트롤러")
public class SafeSwaggerController {

    @Operation(summary = "스웨거 주소 설정", description = "스웨거 주소 redirect 해주는 컨트롤러")
    @GetMapping("/actuator/info")
    public String info() {
        return "redirect:/swagger-ui/index.html";
    }
}

