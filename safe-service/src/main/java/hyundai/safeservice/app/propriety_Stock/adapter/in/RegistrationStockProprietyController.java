package hyundai.safeservice.app.propriety_Stock.adapter.in;

import hyundai.safeservice.app.propriety_Stock.adapter.dto.RequestProprietyDto;
import hyundai.safeservice.app.propriety_Stock.application.port.in.RegistrationStockProprietyUseCase;
import hyundai.safeservice.app.safe_Stock.adapter.dto.AIModifyResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "적정 수량 필터", description = "적정 수량 조회를 위한 필터 API")
public class RegistrationStockProprietyController {

    private final RegistrationStockProprietyUseCase registrationStockUseCase;

    @Operation(summary = "적정 수량 등록", description = "적정 수량 데이터를 등록합니다.")
    @PostMapping("/safe/register/propriety")
    public AIModifyResponse registerSafeStock(@RequestBody RequestProprietyDto requestProprietyDto) {

        registrationStockUseCase.registerSafeStock(requestProprietyDto);

        return AIModifyResponse.from("성공적으로 업데이트 했습니다.");
    }
}