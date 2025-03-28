package hyundai.safeservice.app.propriety_Stock.application.service;

import hyundai.safeservice.app.propriety_Stock.adapter.dto.RequestProprietyDto;
import hyundai.safeservice.app.propriety_Stock.adapter.dto.fein.LogCreateRequest;
import hyundai.safeservice.app.propriety_Stock.adapter.dto.fein.UserDetailResponse;
import hyundai.safeservice.app.propriety_Stock.application.entity.ProprietyStock;
import hyundai.safeservice.app.propriety_Stock.application.port.fein.LogFeinClient;
import hyundai.safeservice.app.propriety_Stock.application.port.fein.PartFeinClient;
import hyundai.safeservice.app.propriety_Stock.application.port.fein.UserFeinClient;
import hyundai.safeservice.app.propriety_Stock.application.port.in.RegistrationStockProprietyUseCase;
import hyundai.safeservice.app.propriety_Stock.application.port.out.RegistrationStockProprietyPort;
import hyundai.safeservice.util.UserIdResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor

public class RegistrationStockProprietyService implements RegistrationStockProprietyUseCase {

    private final RegistrationStockProprietyPort registrationStockProprietyPort;
    private final UserFeinClient userFeinClient;
    private final UserIdResolver userIdResolver;
    private final LogFeinClient logFeinClient;
    private final PartFeinClient partFeinClient;

    @Override
    public void registerSafeStock(RequestProprietyDto requestProprietyDto) {

        Long userId = userIdResolver.getCurrentUserId();

        UserDetailResponse user = userFeinClient.getUserDetail(userId).getBody();

        if(user==null) return;

        ProprietyStock proprietyStock = ProprietyStock.builder()
                .acceptName(user.name())
                .createdAt(LocalDate.now())
                .quantity(requestProprietyDto.safeQuantity())
                .reason(requestProprietyDto.reason())
                .partId(requestProprietyDto.partId())
                .build();

        // Log Service 로그 생성
        String message = "적정 수량이 " + requestProprietyDto.safeQuantity() + " 로 변경되었습니다!!";
        logFeinClient.saveLog(LogCreateRequest.of(userId, message));

        //적정 재고 수정
        partFeinClient.modifyOptimalStock(requestProprietyDto.partId(), requestProprietyDto.safeQuantity());

        // 생성된 safeStock 기록
        registrationStockProprietyPort.stockSave(proprietyStock);
    }
}
