package hyundai.safeservice.app.safe_Stock.adapter.in;

import hyundai.safeservice.app.propriety_Stock.adapter.dto.RequestProprietyDto;
import hyundai.safeservice.app.safe_Stock.adapter.dto.AIModifyResponse;
import hyundai.safeservice.app.safe_Stock.adapter.dto.SafeResponse;
import hyundai.safeservice.app.safe_Stock.application.port.in.RegistrationStockUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "안전 재고 등록", description = "안전 재고 데이터를 등록하는 API")
public class RegistrationStockController {

    private final RegistrationStockUseCase registrationStockUseCase;

    @Operation(summary = "안전 재고 등록", description = "안전 재고 데이터를 등록합니다.")
    @PostMapping("/safe/register/safety")
    public AIModifyResponse registerSafeStock(@RequestBody RequestProprietyDto requestProprietyDto) {

        registrationStockUseCase.registerSafeStock(requestProprietyDto);

        return AIModifyResponse.from("성공적으로 업데이트 했습니다.");
    }
}