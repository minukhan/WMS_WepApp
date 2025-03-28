package hyundai.safeservice.app.safe_Stock.application.service;

import hyundai.safeservice.app.propriety_Stock.adapter.dto.RequestProprietyDto;
import hyundai.safeservice.app.propriety_Stock.adapter.dto.fein.LogCreateRequest;
import hyundai.safeservice.app.propriety_Stock.adapter.dto.fein.UserDetailResponse;
import hyundai.safeservice.app.propriety_Stock.application.port.fein.LogFeinClient;
import hyundai.safeservice.app.propriety_Stock.application.port.fein.PartFeinClient;
import hyundai.safeservice.app.propriety_Stock.application.port.fein.UserFeinClient;
import hyundai.safeservice.app.safe_Stock.application.entity.SafeStock;
import hyundai.safeservice.app.safe_Stock.application.port.in.RegistrationStockUseCase;
import hyundai.safeservice.app.safe_Stock.application.port.out.RegistrationStockPort;
import hyundai.safeservice.util.UserIdResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor

public class RegistrationStockService implements RegistrationStockUseCase {

    private final RegistrationStockPort registrationStockPort;
    private final UserFeinClient userFeinClient;
    private final UserIdResolver userIdResolver;
    private final LogFeinClient logFeinClient;
    private final PartFeinClient partFeinClient;

    @Override
    public void registerSafeStock(RequestProprietyDto requestProprietyDto) {

        Long userId = userIdResolver.getCurrentUserId();

        UserDetailResponse user = userFeinClient.getUserDetail(userId).getBody();

        if(user==null) return;

        SafeStock safeStock = SafeStock.builder()
                .acceptName(user.name())
                .createdAt(LocalDate.now())
                .quantity(requestProprietyDto.safeQuantity())
                .reason(requestProprietyDto.reason())
                .partId(requestProprietyDto.partId())
                .build();

        // Log Service 로그 생성
        String message = "안전수량이 " + requestProprietyDto.safeQuantity() + " 로 변경되었습니다!!";
        logFeinClient.saveLog(LogCreateRequest.of(userId, message));

        // 안전수량 변경 로직 적용
        partFeinClient.modifySafetyStock(requestProprietyDto.partId(),requestProprietyDto.safeQuantity());

        // 생성된 safeStock 기록
        registrationStockPort.stockSave(safeStock);
    }
}
