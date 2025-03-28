package hyundai.safeservice.app.safe_Stock.adapter.in;

import hyundai.safeservice.app.safe_Stock.adapter.dto.AIModifyResponse;
import hyundai.safeservice.app.safe_Stock.adapter.dto.SafeRequestList;
import hyundai.safeservice.app.safe_Stock.application.port.in.ModifyStockUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ModifyStockController {

    private final ModifyStockUseCase modifyStockUseCase;

    @PostMapping("/safe/modify")
    public AIModifyResponse modify(@RequestBody SafeRequestList safeRequestList) {

        modifyStockUseCase.modifyStock(safeRequestList);

        return AIModifyResponse.from("성공적으로 변경 완료 했습니다!");
    }

}
