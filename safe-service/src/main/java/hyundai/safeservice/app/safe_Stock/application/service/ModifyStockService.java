package hyundai.safeservice.app.safe_Stock.application.service;

import hyundai.safeservice.app.propriety_Stock.adapter.dto.fein.UserDetailResponse;
import hyundai.safeservice.app.propriety_Stock.application.entity.ProprietyStock;
import hyundai.safeservice.app.propriety_Stock.application.port.fein.PartFeinClient;
import hyundai.safeservice.app.propriety_Stock.application.port.fein.UserFeinClient;
import hyundai.safeservice.app.safe_Stock.adapter.dto.SafeRequestList;
import hyundai.safeservice.app.safe_Stock.application.entity.SafeStock;
import hyundai.safeservice.app.safe_Stock.application.port.in.ModifyStockUseCase;
import hyundai.safeservice.app.safe_Stock.application.port.out.ModifyStockPort;
import hyundai.safeservice.util.UserIdResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ModifyStockService implements ModifyStockUseCase {

    private final ModifyStockPort modifyStockPort;
    private final UserIdResolver userIdResolver;
    private final UserFeinClient userFeinClient;
    private final PartFeinClient partFeinClient;

    @Transactional
    @Override
    public void modifyStock(SafeRequestList safeRequestList) {

        Long userId = userIdResolver.getCurrentUserId();

        UserDetailResponse user = userFeinClient.getUserDetail(userId).getBody();

        if(user==null) return;

        safeRequestList.safeRequests().stream()
                .forEach(safeRequest -> {

                    SafeStock safeStock = SafeStock.builder()
                            .acceptName(user.name())
                            .createdAt(LocalDate.now())
                            .quantity(safeRequest.modifySafeQuantity())
                            .reason("AI 의 수정 추천값으로 변경")
                            .partId(safeRequest.partId())
                            .build();

                    modifyStockPort.save(safeStock);

                    ProprietyStock proprietyStock = ProprietyStock.builder()
                            .acceptName(user.name())
                            .createdAt(LocalDate.now())
                            .quantity(safeRequest.modifyProprietyQuantity())
                            .reason("AI 의 수정 추천값으로 변경")
                            .partId(safeRequest.partId())
                            .build();

                    modifyStockPort.savePro(proprietyStock);


                    //적정 재고 수정
                    partFeinClient.modifyOptimalStock(safeRequest.partId(), safeRequest.modifyProprietyQuantity());


                    //안전 재고 수정
                    ResponseEntity<String> safe = partFeinClient.modifySafetyStock(safeRequest.partId(), safeRequest.modifySafeQuantity());




                });

    }
}
